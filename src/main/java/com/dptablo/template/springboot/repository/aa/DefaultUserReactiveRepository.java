package com.dptablo.template.springboot.repository.aa;

import com.dptablo.template.springboot.model.mongo.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultUserReactiveRepository extends ReactiveMongoRepository<User, String> {
}
