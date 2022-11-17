package com.dptablo.template.springboot.repository.reactive.r2dbc;

import com.dptablo.template.springboot.configuration.FlywayConfiguration;
import com.dptablo.template.springboot.configuration.PostgresR2dbcConfiguration;
import com.dptablo.template.springboot.model.r2dbc.User;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@ActiveProfiles("local")
@ContextConfiguration(classes = {
        PostgresR2dbcConfiguration.class,
        FlywayConfiguration.class,
        UserR2dbcRepository.class})
@EnableAutoConfiguration
class UserR2dbcRepositoryLocalTest {
    @Autowired
    private UserR2dbcRepository userR2dbcRepository;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    void beforeEach() {
        flyway.undo();
    }

    @Test
    void getAllUsers() {
        //given
        var now = LocalDateTime.now();

        var user = User.builder()
                .userId("user8585834")
                .password("12345")
                .name("사용자8585834")
                .phoneNumber("01099998888")
                .createDate(now)
                .updateDate(now)
                .build();

//        userR2dbcRepository.save(user).block();
        var a = userR2dbcRepository.save3(user).block();

        //when
        List<User> users = userR2dbcRepository.getAllUsers()
                .collectList().block();

        //then
        assertThat(users.size()).isGreaterThanOrEqualTo(1);
    }
}