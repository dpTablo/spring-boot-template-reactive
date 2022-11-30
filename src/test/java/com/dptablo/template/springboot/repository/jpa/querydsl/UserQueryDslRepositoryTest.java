package com.dptablo.template.springboot.repository.jpa.querydsl;

import com.dptablo.template.springboot.configuration.JpaConfiguration;
import com.dptablo.template.springboot.configuration.TestQueryDslConfiguration;
import com.dptablo.template.springboot.model.entity.User;
import com.dptablo.template.springboot.repository.jpa.UserRepository;
import com.dptablo.template.springboot.test.support.DataSourcePostgresTestSupportExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("tc")
@Testcontainers
@ExtendWith(DataSourcePostgresTestSupportExtension.class)
@Import(TestQueryDslConfiguration.class)
@ContextConfiguration(classes = {
        JpaConfiguration.class, UserQueryDslRepository.class, UserRepository.class
})
@EnableAutoConfiguration
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
                .password("1234")
                .build();
        userRepository.save(user1);

        User user2 = User.builder()
                .userId("user2")
                .name("스티브")
                .password("1234")
                .build();
        userRepository.save(user2);

        User user3 = User.builder()
                .userId("user3")
                .name("박길동")
                .password("1234")
                .build();
        userRepository.save(user3);

        User user4 = User.builder()
                .userId("user4")
                .name("문무길")
                .password("1234")
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