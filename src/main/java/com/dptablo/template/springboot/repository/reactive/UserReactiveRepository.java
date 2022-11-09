package com.dptablo.template.springboot.repository.reactive;

import com.dptablo.template.springboot.model.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserReactiveRepository extends ReactiveCrudRepository<User, String> {
    @Query("SELECT * FROM t_user")
    Flux<User> getAllUsers();
}
