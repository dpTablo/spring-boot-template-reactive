package com.dptablo.template.springboot.repository.mongodb.reactive;

import com.dptablo.template.springboot.model.mongo.reactive.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultUserReactiveRepository extends ReactiveMongoRepository<User, String> {
}
