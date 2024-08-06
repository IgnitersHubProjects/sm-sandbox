package com.igniters.sm.sandbox.json_redis;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igniters.sm.sandbox.utility.RedisOperation;

@Service
public class JsonParserService {

    @Value("${json.file.path}")
    private String filepath;


    @Autowired
    private RedisOperation redisOperation;

    

/* 
    ? For later use -> api response handle 
   public InstrumentResponse parseJsonResponse(String apiResponse) throws JsonMappingException, JsonProcessingException{
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(apiResponse,InstrumentResponse.class);
   }

    public List<Instrument> getParsedInstrumentsFromAPI(String apiResponse) throws IOException{
        InstrumentResponse response = parseJsonResponse(apiResponse);
        List<Instrument> instruments = response.getParsedResult();
        for(Instrument instrument : instruments){
           redisOperation.saveItemInCache(instrument, instrument.getDescription(),redisOperation.expirationTime() );
            System.out.println("saved");
        }
        return instruments;
    }
    ? for later use 
     
*/



     
    //? for loading file to memenory( have to remove in future)
    public InstrumentResponse parseJsonFile() throws IOException{
        Path path = Paths.get(filepath);
        String content = Files.readString(path);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(content, InstrumentResponse.class);
    }


    //? to parse and save data to redis (have to remove in future)
    public List<Instrument> getParsedInstruments() throws IOException{
        InstrumentResponse response = parseJsonFile();
        List<Instrument> instruments = response.getParsedResult();
        for(Instrument instrument : instruments){
           // instrumentRepository.save(instrument);
           redisOperation.saveItemInCache(instrument, instrument.getDescription(), redisOperation.expirationTime());
            // System.out.println("saved");
        }
        return instruments;
    }


    //! to fetch data from redis By Pattern
    public List<Instrument> getInstrumentsFromRedis(String pattern){
         return redisOperation.findByPattern(pattern);
    }


    //! to fetch data by Key
    public Instrument findByKey(String key){
        return redisOperation.findByKey(key);
    }
    
}
