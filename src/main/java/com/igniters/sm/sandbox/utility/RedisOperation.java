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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.igniters.sm.sandbox.json_redis.Instrument;

@Component
public class RedisOperation {

    @Autowired
    public RedisTemplate<String, Object> redisTemplate;

    public Instant expirationTime() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.of(23, 59);
        LocalDateTime datatime = LocalDateTime.of(date, time);
        ZonedDateTime zonedDateTime = datatime.atZone(ZoneId.of("GMT"));
        return zonedDateTime.toInstant();
    }

    public void saveItemInCache(Instrument instrument, String key, Instant expiryTime) {
        try {
            redisTemplate.opsForValue().set(key, instrument);
            redisTemplate.expireAt(key, expiryTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTokentoCache(String key, String token, Instant expiryTime) {
        try {
            redisTemplate.opsForValue().set(key, token);
            redisTemplate.expireAt(key, expiryTime);
            System.out.println("data added");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Instrument> findByPattern(String pattern) {
        List<Instrument> instruments = new ArrayList<>();
        try {
            Set<String> keys = redisTemplate.keys(pattern + "*");
            for (String key : keys) {
                if (key.startsWith(pattern) && !key.equals("IIFLSession") && !key.equals("IsInsturmentPresent")) {
                    // System.out.println(" pattern : " + pattern);
                    Instrument instrument = (Instrument) redisTemplate.opsForValue().get(key);
                    // System.out.println(" pattern : " + instrument.getDescription());
                    instruments.add(instrument);
                    if (instruments.size() > 25)
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instruments;
    }

    public <T> T findByKey(String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return null;
            }
            if (type == String.class && value instanceof String) {
                return type.cast(value);
            } else if (type == Instrument.class && value instanceof Instrument) {
                return type.cast(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
