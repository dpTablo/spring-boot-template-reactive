package com.dptablo.template.springboot.test.support;

import com.dptablo.template.springboot.configuration.FlywayConfiguration;
import com.dptablo.template.springboot.model.r2dbc.User;
import com.dptablo.template.springboot.repository.reactive.r2dbc.DefaultUserR2DbcRepository;
import org.junit.jupiter.api.*;
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
@ContextConfiguration(classes = {FlywayConfiguration.class, DefaultUserR2DbcRepository.class})
@EnableAutoConfiguration
public class TestContainersPostgresR2DbcTestExample {
    @Autowired
    private DefaultUserR2DbcRepository defaultUserR2DbcRepository;

    @Test
    void test() {
        //given
        var now = LocalDateTime.now();

        var user1 = User.builder()
                .userId("user1")
                .password("1234")
                .name("사용자1")
                .phoneNumber("01011112222")
                .createDate(now)
                .updateDate(now)
                .build();
        var insertedUser1 = defaultUserR2DbcRepository.save(user1).block();

        //when
        List<User> allUsers = defaultUserR2DbcRepository.getAllUsersByQuery()
                .collectList()
                .block();

        //then
        assertThat(allUsers).isNotNull();
        assertThat(allUsers.size()).isEqualTo(1);
        assertThat(allUsers.contains(insertedUser1)).isTrue();
    }

    @Test
    void test2() {
        //given
        var now = LocalDateTime.now();

        var user1 = User.builder()
                .userId("user1")
                .password("1234")
                .name("사용자1")
                .phoneNumber("01011112222")
                .createDate(now)
                .updateDate(now)
                .build();
        var insertedUser1 = defaultUserR2DbcRepository.save(user1).block();

        //when
        List<User> allUsers = defaultUserR2DbcRepository.getAllUsersByQuery()
                .collectList()
                .block();

        //then
        assertThat(allUsers).isNotNull();
        assertThat(allUsers.size()).isEqualTo(1);
        assertThat(allUsers.contains(insertedUser1)).isTrue();
    }
}


