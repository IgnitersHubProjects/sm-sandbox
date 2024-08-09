package com.igniters.sm.sandbox.service.impl;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igniters.sm.sandbox.model.Instrument;
import com.igniters.sm.sandbox.utility.InstrumentResponse;
import com.igniters.sm.sandbox.utility.RedisOperation;

@Service
public class JsonParserService {


    @Autowired
    private RedisOperation redisOperation;

    //! api response handle
    public InstrumentResponse parseJsonResponse(String apiResponse)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(apiResponse, InstrumentResponse.class);
    }

    public void getParsedInstrumentsFromAPI(String apiResponse) throws IOException {
        InstrumentResponse response = parseJsonResponse(apiResponse);
        List<Instrument> instruments = response.getParsedResult();
        redisOperation.saveTokentoCache("IsInsturmentPresent", "yes", redisOperation.expirationTime());
        for (Instrument instrument : instruments) {
            redisOperation.saveItemInCache(instrument, instrument.getDescription(), redisOperation.expirationTime());
        }
    }

    // ! to fetch data from redis By Pattern
    public List<Instrument> getInstrumentsFromRedis(String pattern) {
        return redisOperation.findByPattern(pattern);
    }

    // ! to fetch data by Key
    public Instrument findByKey(String key) {
        return redisOperation.findByKey(key, Instrument.class);
    }

    // ! to get Instrument lotSize and price to calculate its order amount
    public String getLotAmount(String description) {
        Instrument instrument = redisOperation.findByKey(description, Instrument.class);
        int lotSize = instrument.getLotSize();
        float price = Float.parseFloat(instrument.getPriceBandHigh());
        String ans = Float.toString(lotSize * price);
        return ans;

    }

}
