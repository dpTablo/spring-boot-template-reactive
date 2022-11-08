package com.dptablo.template.springboot.reactive.repository.reactive.r2dbc;

import com.dptablo.template.springboot.reactive.configuration.PostgresReactiveConfiguration;
import com.dptablo.template.springboot.reactive.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataR2dbcTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {PostgresReactiveConfiguration.class, UserR2DbcRepository.class})
@EnableAutoConfiguration
class UserR2DbcRepositoryTest {
    @Autowired
    private UserR2DbcRepository userR2DbcRepository;

    @Test
    void getAllUsers() {
        //given

        //when
        List<User> allUsers = userR2DbcRepository.getAllUsersByQuery()
                .collectList()
                .block();

        //then
        assertThat(allUsers.size()).isGreaterThanOrEqualTo(1);
    }
}