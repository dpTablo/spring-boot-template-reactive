package com.dptablo.template.springboot.reactive.repository.reactive;

import com.dptablo.template.springboot.reactive.model.entity.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserCustomReactiveRepository {
    Flux<User> getAllUsersByQuery();
}
