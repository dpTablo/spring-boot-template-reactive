package com.dptablo.template.springboot.service.r2dbc;

import com.dptablo.template.springboot.model.r2dbc.User;
import com.dptablo.template.springboot.repository.reactive.r2dbc.UserR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DefaultUserR2dbcService implements UserR2dbcService {
    private final UserR2dbcRepository userR2dbcRepository;

    @Override
    @Transactional(transactionManager = "postgresTransactionManager")
    public Mono<User> addUser(User user) {
        Mono<Integer> count = userR2dbcRepository.save2(user);
        return null;
    }
}
