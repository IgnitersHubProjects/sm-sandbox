package com.igniters.sm.sandbox.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.igniters.sm.sandbox.json_redis.QuotesData;

public interface IIFLService {

    public void checkIIFLtoken() throws JsonMappingException, JsonProcessingException;

    public String getIIFLToken();

    public void checkInstrumentData() throws IOException;

    public QuotesData getQuoteData(int exchangeInstrumentId);
    
}
