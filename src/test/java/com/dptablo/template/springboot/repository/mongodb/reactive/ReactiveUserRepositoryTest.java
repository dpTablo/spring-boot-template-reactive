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
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("tc")
@Testcontainers
@ExtendWith(MongoDBTestSupportExtension.class)
@ContextConfiguration(classes = {
        ReactiveMongoDBConfiguration.class,
        ReactiveUserRepository.class,
})
@EnableAutoConfiguration
class ReactiveUserRepositoryTest implements TestContainersReactiveMongoDBTest {
    @Autowired
    @Qualifier("reactiveUserRepository")
    private ReactiveUserRepository reactiveUserRepository;

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
        var savedUser = reactiveUserRepository.save(user).block();

        //then
        assertThat(savedUser).isEqualTo(user);
    }

    @DisplayName("User 데이터 변경 테스트")
    @Test
    public void updateTest() {
        //given
        var user = User.builder()
                .userId("admin")
                .password("1234")
                .name("관리자")
                .phoneNumber("01099998888")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        var savedUser = reactiveUserRepository.save(user).blockOptional()
                .orElseThrow(NullPointerException::new);

        savedUser.setName("일반유저1");
        savedUser.setPassword("5678");
        savedUser.setPhoneNumber("01011112222");
        savedUser.setUpdateDate(LocalDateTime.of(2022, 1, 2, 10, 20, 30));
        savedUser.setCreateDate(LocalDateTime.of(2022, 12, 10, 11, 10, 20));

        //when
        reactiveUserRepository.save(savedUser).block();
        var updatedUser = reactiveUserRepository.findById("admin").block();

        //then
        assertThat(updatedUser).isEqualTo(savedUser);
    }

    @DisplayName("User 정보 삭제 테스트")
    @Test
    public void deleteTest() {
        //given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var user = User.builder()
                .userId("admin")
                .password(passwordEncoder.encode("1234"))
                .name("관리자")
                .phoneNumber("01088889999")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        var savedUser = reactiveUserRepository.save(user).blockOptional()
                .orElseThrow(NullPointerException::new);

        //when
        reactiveUserRepository.delete(savedUser).block();
        var foundUser = reactiveUserRepository.findById(user.getUserId()).block();

        //then
        assertThat(foundUser).isNull();
    }

    @DisplayName("User 이름 검색 조회 테스트")
    @Test
    public void getUserListBySearchingNameTest() {
        //given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        var user1 = User.builder()
                .userId("user1")
                .password(passwordEncoder.encode("1234"))
                .name("김길동")
                .phoneNumber("01088889999")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        reactiveUserRepository.save(user1).block();

        var user2 = User.builder()
                .userId("user2")
                .password(passwordEncoder.encode("1234"))
                .name("유재석")
                .phoneNumber("01088889999")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        reactiveUserRepository.save(user2).block();

        var user3 = User.builder()
                .userId("user3")
                .password(passwordEncoder.encode("1234"))
                .name("이한길")
                .phoneNumber("01088889999")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        reactiveUserRepository.save(user3).block();

        var user4 = User.builder()
                .userId("user4")
                .password(passwordEncoder.encode("1234"))
                .name("김구라")
                .phoneNumber("01088889999")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        reactiveUserRepository.save(user4).block();

        //when
        Flux<User> usersFlux = reactiveUserRepository.findByNameLike("길");

        //then
        StepVerifier
                .create(usersFlux)
                .recordWith(ArrayList::new)
                .expectNextCount(2)
                .consumeRecordedWith(record -> {
                        var list = record.stream().map(User::getName).collect(Collectors.toList());
                        assertThat(list).contains("김길동", "이한길");
                })
                .verifyComplete();
    }
}