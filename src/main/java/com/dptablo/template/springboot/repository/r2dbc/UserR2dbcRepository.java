package com.dptablo.template.springboot.repository.r2dbc;

import com.dptablo.template.springboot.model.r2dbc.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserR2dbcRepository extends R2dbcRepository<User, String> {
    @Query("SELECT * FROM t_user")
    Flux<User> getAllUsers();

    @Query("INSERT INTO t_user(user_id, password, name, phone_number) VALUES (:#{#user.userId}, :#{#user.password}, :#{#user.name}, :#{#user.phoneNumber})")
    Mono<Void> insert(@Param("user") User user);
}
