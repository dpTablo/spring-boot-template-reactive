package com.dptablo.template.springboot.repository.reactive.r2dbc;

import com.dptablo.template.springboot.model.r2dbc.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserCustomR2dbcRepository {
    Flux<User> getAllUsersByQuery();
    Mono<User> save(User user);
}
