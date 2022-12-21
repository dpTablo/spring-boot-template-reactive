package com.dptablo.template.springboot.repository.mongodb.reactive;

import com.dptablo.template.springboot.model.mongo.reactive.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ReactiveUserRepository extends ReactiveCrudRepository<User, String> {
    Flux<User> findByNameLike(String name);
}
