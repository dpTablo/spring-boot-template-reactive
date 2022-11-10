package com.dptablo.template.springboot.repository.reactive.r2dbc;

import com.dptablo.template.springboot.model.r2dbc.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserR2dbcRepository extends ReactiveCrudRepository<User, String> {
        @Query("SELECT * FROM t_user")
    Flux<User> getAllUsers();

//    @Modifying
//    @Query("INSERT INTO t_user(user_id, password) VALUES (:userId, :password)")
//    Mono<User> insert(String userId, String password);
//
//    @Modifying
//    @Query("UPDATE t_user SET password = :password WHERE user_id = :userId")
//    Mono<User> update(String userId, String password);
}
