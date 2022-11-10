package com.dptablo.template.springboot.repository.reactive.r2dbc;

import com.dptablo.template.springboot.configuration.FlywayConfiguration;
import com.dptablo.template.springboot.model.r2dbc.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataR2dbcTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {FlywayConfiguration.class, DefaultUserR2DbcRepository.class})
@EnableAutoConfiguration
class DefaultUserR2DbcRepositoryTest {
    @Autowired
    private DefaultUserR2DbcRepository defaultUserR2DbcRepository;

    @Test
    void getAllUsersByQuery() {
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
        var insertedUser1 = defaultUserR2DbcRepository.insert(user1).block();

        var user2 = User.builder()
                .userId("user2")
                .password("3333")
                .name("사용자2")
                .phoneNumber("01033334444")
                .createDate(now)
                .updateDate(now)
                .build();
        var insertedUser2 = defaultUserR2DbcRepository.insert(user2).block();

        //when
        List<User> allUsers = defaultUserR2DbcRepository.getAllUsersByQuery()
                .collectList()
                .block();

        //then
        assertThat(allUsers.size()).isEqualTo(2);
        assertThat(allUsers.contains(insertedUser1)).isTrue();
        assertThat(allUsers.contains(insertedUser2)).isTrue();
    }
}