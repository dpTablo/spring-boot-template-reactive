package com.dptablo.template.springboot.repository.reactive.mongodb;

import com.dptablo.template.springboot.model.mongo.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserReactiveRepository extends ReactiveCrudRepository<User, String> {
    Flux<User> findByNameLike(String name);
}
