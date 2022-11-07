package com.dptablo.template.springboot.repository.jpa;

import com.dptablo.template.springboot.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
