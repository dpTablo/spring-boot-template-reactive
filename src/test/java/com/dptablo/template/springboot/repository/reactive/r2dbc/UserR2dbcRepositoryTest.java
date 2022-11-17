package com.dptablo.template.springboot.repository.reactive.r2dbc;

import com.dptablo.template.springboot.R2dbcPostgreSQLTestSupportExtension;
import com.dptablo.template.springboot.configuration.FlywayConfiguration;
import com.dptablo.template.springboot.configuration.PostgresR2dbcConfiguration;
import com.dptablo.template.springboot.model.r2dbc.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataR2dbcTest
@ActiveProfiles("tc")
@Testcontainers
@ExtendWith(R2dbcPostgreSQLTestSupportExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ContextConfiguration(classes = {
        PostgresR2dbcConfiguration.class,
        FlywayConfiguration.class,
        UserR2dbcRepository.class})
@EnableAutoConfiguration
class UserR2dbcRepositoryTest {
    @Autowired
    private UserR2dbcRepository userR2dbcRepository;
    
    @Test
    void getAllUsers() {
        //given
        var now = LocalDateTime.now();

        var user = User.builder()
                .userId("user8585834")
                .password("12345")
                .name("사용자1")
                .phoneNumber("01099998888")
                .createDate(now)
                .updateDate(now)
                .build();

        var result = userR2dbcRepository.save2(user).block();

        //when
        List<User> users = userR2dbcRepository.getAllUsers()
                .collectList().block();

        //then
        assertThat(users.size()).isGreaterThanOrEqualTo(1);
    }
}