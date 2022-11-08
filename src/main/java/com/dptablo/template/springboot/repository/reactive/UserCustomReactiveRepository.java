package com.dptablo.template.springboot.repository.reactive;

import com.dptablo.template.springboot.model.entity.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserCustomReactiveRepository {
    Flux<User> getAllUsersByQuery();
}
