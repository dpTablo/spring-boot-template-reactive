package com.dptablo.template.springboot.reactive.repository.jpa;

import com.dptablo.template.springboot.reactive.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCustomRepository {
    List<User> getUserListBySearchingName(String name);
}
