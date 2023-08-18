package com.bezkoder.spring.login.models;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ArticleSerializer extends JsonSerializer<Article> {
    @Override
    public void serialize(Article article, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(article.getLibelle());
    }
}
