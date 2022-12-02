package com.dptablo.template.springboot.repository.reactive.mongodb;

import com.dptablo.template.springboot.model.mongo.User;
import reactor.core.publisher.Flux;

public interface UserReactiveRepository {
    Flux<User> getUserListBySearchingName(String name);
}
