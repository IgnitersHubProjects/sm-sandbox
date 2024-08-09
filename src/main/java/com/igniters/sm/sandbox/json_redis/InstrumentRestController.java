package com.igniters.sm.sandbox.json_redis;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class InstrumentRestController {

    @Autowired
    private JsonParserService jsonParserService;

    // ! to return order amoutn to user in ui form for buy and sell
    @GetMapping("/getLotSize")
    public ResponseEntity<?> getLotSize(@RequestParam String description) {
        String lotSize = jsonParserService.getLotAmount(description);
        // log.info("lotSize : "+ lotSize);
        if (lotSize != null) {
            // log.info("not null");
            return ResponseEntity.ok(Collections.singletonMap("lotAmount", Float.parseFloat(lotSize)));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lot size not found");
        }
    }
}
