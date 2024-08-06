package com.igniters.sm.sandbox.utility;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import com.igniters.sm.sandbox.json_redis.Instrument;

@Component
public class RedisOperation {


    @Autowired
    public RedisTemplate<String,Object> redisTemplate;

    public static Instant expirationTime(){
        LocalDate date  = LocalDate.now();
        LocalTime time = LocalTime.of(23,59);
        LocalDateTime datatime = LocalDateTime.of(date,time);
        ZonedDateTime zonedDateTime = datatime.atZone(ZoneId.of("GMT"));
        return zonedDateTime.toInstant();
    }


    public void saveItemInCache(Instrument instrument, String key, Instant expiryTime){
        try{
        
            redisTemplate.opsForValue().set(key, instrument);
            redisTemplate.expireAt(key, expiryTime);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<Instrument> findByPattern(String pattern) {
        List<Instrument> instruments = new ArrayList<>();
       try{
         Set<String> keys = redisTemplate.keys(pattern + "*");
         
        
         for (String key : keys) {
            if(key.startsWith(pattern)){
                Instrument instrument = (Instrument) redisTemplate.opsForValue().get(key);
                instruments.add(instrument);

                if(instruments.size() > 25) break;
            }
           
            }  
        
       }catch(Exception e){
        e.printStackTrace();
       }
       return instruments;
    }

  


    public Instrument findByKey(String key) {
       
       try{
       
          return (Instrument) redisTemplate.opsForValue().get(key);
              
        
       }catch(Exception e){
        e.printStackTrace();
       }
       return null;
       
    }



}
