package com.bezkoder.spring.login.models;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ClientDeserializer extends JsonDeserializer<Client> {

    @Override
    public Client deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        try {
            String value = jsonParser.readValueAs(String.class);
            return new Client(value);
        } catch (Exception e) {
            throw new JsonMappingException(jsonParser, "Error deserializing Article", e);
        }
    }
    
}
