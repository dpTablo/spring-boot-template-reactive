package com.dptablo.template.springboot.repository.mongodb.reactive;

import com.dptablo.template.springboot.configuration.ReactiveMongoDBConfiguration;
import com.dptablo.template.springboot.model.mongo.reactive.User;
import com.dptablo.template.springboot.test.support.MongoDBTestSupportExtension;
import com.dptablo.template.springboot.test.support.TestContainersReactiveMongoDBTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("tc")
@Testcontainers
@ExtendWith(MongoDBTestSupportExtension.class)
@ContextConfiguration(classes = {
        ReactiveMongoDBConfiguration.class,
        DefaultUserReactiveRepository.class,
})
@EnableAutoConfiguration
class DefaultReactiveUserRepositoryTest implements TestContainersReactiveMongoDBTest {
    @Autowired
    @Qualifier("defaultUserReactiveRepository")
    private DefaultUserReactiveRepository defaultUserReactiveRepository;

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public ReactiveMongoTemplate getReactiveMongoTemplate() {
        return reactiveMongoTemplate;
    }

    @DisplayName("User 데이터 저장 기본 테스트")
    @Test
    public void saveTest() {
        //given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var user = User.builder()
                .userId("admin")
                .password(passwordEncoder.encode("1234"))
                .name("관리자")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        //when
        var savedUser = defaultUserReactiveRepository.save(user).block();

        //then
        assertThat(savedUser).isEqualTo(user);
    }

    @DisplayName("User 데이터 저장 기본 테스트2")
    @Test
    public void saveTest2() {
        //given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var user = User.builder()
                .userId("admin")
                .password(passwordEncoder.encode("1234"))
                .name("관리자")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        //when
        var savedUser = defaultUserReactiveRepository.save(user).block();

        //then
        assertThat(savedUser).isEqualTo(user);
    }
}