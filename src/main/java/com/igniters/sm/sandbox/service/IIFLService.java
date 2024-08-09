package com.igniters.sm.sandbox.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface IIFLService {
    public void saveIIFLtoken() throws JsonMappingException, JsonProcessingException;

    public String getIIFLToken();

    public void saveInstrumentData() throws IOException;
    
}
