package com.igniters.sm.sandbox.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igniters.sm.sandbox.json_redis.JsonParserService;
import com.igniters.sm.sandbox.json_redis.QuotesData;
import com.igniters.sm.sandbox.service.IIFLService;
import com.igniters.sm.sandbox.utility.RedisOperation;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class IIFLServiceImpl implements IIFLService {

    @Autowired
    private RestTemplate restTemplate;

   
    @Autowired
    private RedisOperation redisOperation;

    @Autowired
    private JsonParserService jsonParserService;

    @Value("${IIFL.secretKey}")
    private String secretKey;

    @Value("${IIFL.appKey}")
    private String appKey;

    @Value("${IIFL.loginUrl}")
    private String loginurl;

    @Value("${IIFL.masterUrl}")
    private String masterUrl;

    @Value("${IIFL.quoteUrl}")
    private String quoteUrl;


    public void checkIIFLtoken() throws JsonMappingException, JsonProcessingException {

        Object object = redisOperation.findByKey("IIFLSession", String.class);
        if (object != null) {
            System.out.println("Already present");
        } else {
            System.out.println("Not present");
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            String requestBody = String.format("{\"secretKey\":\"%s\", \"appKey\":\"%s\"}", secretKey, appKey);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(loginurl, HttpMethod.POST, entity, String.class);
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            JsonNode resultNode = jsonNode.get("result");
            String token = resultNode.get("token").asText();
            System.out.println("Token: " + token);
            redisOperation.saveTokentoCache("IIFLSession", token, redisOperation.expirationTime());
        }

    }

    @Override
    public String getIIFLToken() {
        // String token = (String) redisTemplate.opsForValue().get("IIFLSession");
        String token = redisOperation.findByKey("IIFLSession", String.class);
        System.out.println("token get from redis : " + token);
        return token;
    }

    @Override
    public void checkInstrumentData() throws IOException {
        Object object = redisOperation.findByKey("IsInsturmentPresent", String.class);
        if (object != null) {
            System.out.println("Instruments present");
        } else {
            System.out.println("Instruments Not present");
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            String requestBody = String.format("{\"exchangeSegmentList\": [%s]}", "\"NSEFO\"");
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(masterUrl, HttpMethod.POST, entity, String.class);
            String responseBody = response.getBody();
            jsonParserService.getParsedInstrumentsFromAPI(responseBody);



            // JsonNode jsonNode = objectMapper.readTree(responseBody);
            // JsonNode resultNode = jsonNode.get("result");
            // String token = resultNode.get("token").asText();
            // System.out.println("got response form master api " + responseBody );
            // redisOperation.saveTokentoCache("IIFLSession", token,
            // redisOperation.expirationTime());

        }

    }

    @Override
    public QuotesData getQuoteData(int exchangeInstrumentId) {
        System.out.println("inside getQuoteData ");
        Object object = redisOperation.findByKey("IIFLSession", String.class);
        String token = object.toString();
        System.out.println("inside getQuoteData token :  " + token);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", token);

        String requestBody = String.format(
                "{\n" +
                        "  \"instruments\": [\n" +
                        "    {\n" +
                        "      \"exchangeSegment\": 2,\n" +
                        "      \"exchangeInstrumentID\": %d\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"xtsMessageCode\": 1501,\n" +
                        "  \"publishFormat\": \"JSON\"\n" +
                        "}",
                exchangeInstrumentId);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(quoteUrl, HttpMethod.POST, entity, String.class);
        String responseBody = response.getBody();


        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode resultNode = rootNode.path("result");
            JsonNode listQuotesNode = resultNode.path("listQuotes");

            List<String> jsonStrings = new ArrayList<>();
            if (listQuotesNode.isArray()) {
                for (JsonNode node : listQuotesNode) {
                    jsonStrings.add(node.asText());
                }
            } else {
                jsonStrings.add(listQuotesNode.asText());
            }

            List<QuotesData> quotesDataList = new ArrayList<>();
            for (String jsonString : jsonStrings) {

                QuotesData quotesData = objectMapper.readValue(jsonString, QuotesData.class);
                quotesDataList.add(quotesData);
            }

            System.out.println(quotesDataList.get(0));
            return quotesDataList.get(0);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
