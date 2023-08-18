package com.bezkoder.spring.login.models;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.bezkoder.spring.login.Exception.ResourceNotFoundException;
import com.bezkoder.spring.login.repository.ArticleRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


public class ArticleDeserializer extends JsonDeserializer<Article> {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        Long articleId = jsonParser.getValueAsLong();
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found with id: " + articleId));
    }
    
}
