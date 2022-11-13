package com.dptablo.template.springboot.service.r2dbc;

import com.dptablo.template.springboot.model.r2dbc.User;
import reactor.core.publisher.Mono;

public interface UserR2dbcService {
    Mono<User> addUser(final User user);
}
