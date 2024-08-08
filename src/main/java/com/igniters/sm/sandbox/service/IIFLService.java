package com.igniters.sm.sandbox.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface IIFLService {
    public void saveIIFLtoken() throws JsonMappingException, JsonProcessingException;

    public String getIIFLToken();

    public void saveInstrumentData();
    
}
