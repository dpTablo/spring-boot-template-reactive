package com.dptablo.template.springboot.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.dptablo.template.springboot.repository.mongodb.reactive")
@RequiredArgsConstructor
public class ReactiveMongoDBConfiguration {
    private final MongoProperties mongoProperties;

    private final MongoClient mongoClient;

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoClient, mongoProperties.getDatabase());
    }
}