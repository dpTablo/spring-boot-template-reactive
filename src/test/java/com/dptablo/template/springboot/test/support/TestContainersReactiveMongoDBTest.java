package com.dptablo.template.springboot.test.support;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

public interface TestContainersReactiveMongoDBTest {
    ReactiveMongoTemplate getReactiveMongoTemplate();
}
