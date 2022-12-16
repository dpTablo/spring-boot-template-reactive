package com.dptablo.template.springboot.service.mongo.reactive;

import com.dptablo.template.springboot.model.mongo.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveUserService {
    Mono<User> getUser(String userId);
    Flux<User> searchUserByNameLike(String keyword);
    Mono<User> addUser(User user);
    Mono<User> updateUser(String userId, User value);
    Mono<Boolean> deleteUser(String userId);
}
