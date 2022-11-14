package com.dptablo.template.springboot.service.r2dbc;

import com.dptablo.template.springboot.model.r2dbc.User;
import com.dptablo.template.springboot.repository.reactive.r2dbc.UserCustomR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DefaultUserR2dbcService implements UserR2dbcService {
    @Qualifier("defaultUserR2DbcRepository")
    private final UserCustomR2dbcRepository userCustomR2dbcRepository;

    @Override
    @Transactional(transactionManager = "postgresTransactionManager")
    public Mono<User> addUser(User user) {
        return userCustomR2dbcRepository.save(user);
    }
}
