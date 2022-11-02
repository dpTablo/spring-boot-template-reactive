package com.dptablo.template.springboot.repository.querydsl;

import com.dptablo.template.springboot.configuration.TestQueryDslConfiguration;
import com.dptablo.template.springboot.model.entity.User;
import com.dptablo.template.springboot.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@ActiveProfiles("test")
@Import(TestQueryDslConfiguration.class)
class UserQueryDslRepositoryTest {
    @Autowired
    private UserQueryDslRepository userQueryDslRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 목록 조회 - contains '길' 3건 조회")
    @Test
    void getUserListBySearchingName() {
        //given
        User user1 = User.builder()
                .userId("user1")
                .name("홍길동")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .userId("user2")
                .name("스티브")
                .build();
        userRepository.save(user2);

        User user3 = User.builder()
                .userId("user3")
                .name("박길동")
                .build();
        userRepository.save(user3);

        User user4 = User.builder()
                .userId("user4")
                .name("문무길")
                .build();
        userRepository.save(user4);

        //when
        List<User> userList = userQueryDslRepository.getUserListBySearchingName("길");

        //then
        assertThat(userList.size()).isEqualTo(3);
        assertThat(userList.contains(user1)).isTrue();
        assertThat(userList.contains(user3)).isTrue();
        assertThat(userList.contains(user4)).isTrue();
    }
}