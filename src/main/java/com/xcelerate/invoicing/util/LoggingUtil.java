package com.xcelerate.invoicing.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class LoggingUtil {

    private LoggingUtil(){
        //Not allowed
    }

    public static String getString(Object dto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(dto);
            return json;
        } catch (Exception e) {
            return "Error serializing DTO"+e.getMessage();
        }
    }


}
