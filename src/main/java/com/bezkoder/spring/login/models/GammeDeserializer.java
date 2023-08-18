package com.bezkoder.spring.login.models;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class GammeDeserializer extends JsonDeserializer<Gamme> {

    @Override
    public Gamme deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        String value = jsonParser.readValueAs(String.class);
        // Initialize the Machine instance using the provided String value
        return new Gamme(value);
    }
    
}
