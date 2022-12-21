package com.dptablo.template.springboot.service.mongo.reactive;

import com.dptablo.template.springboot.model.mongo.reactive.User;
import com.dptablo.template.springboot.repository.mongodb.reactive.ReactiveUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultReactiveUserServiceTest {
    @InjectMocks
    private DefaultReactiveUserService userService;

    @Mock
    private ReactiveUserRepository reactiveUserRepository;

    @DisplayName("getUser 조회 성공")
    @Test
    void getUserTest() {
        //given
        var user1 = User.builder()
                .userId("user1")
                .password("1234")
                .name("유저1")
                .phoneNumber("01099998888")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        given(reactiveUserRepository.findById(user1.getUserId()))
                .willReturn(Mono.just(user1));

        //when
        User user = userService.getUser(user1.getUserId()).block();

        //then
        verify(reactiveUserRepository, times(1)).findById(user1.getUserId());
        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(user1);
    }

    @DisplayName("사용자명 검색 조회 성공")
    @Test
    void searchUserByNameLikeTest() {
        //given
        var user1 = User.builder()
                .userId("user2")
                .password("2222")
                .name("신동엽")
                .phoneNumber("01012341234")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        var user2 = User.builder()
                .userId("user3")
                .password("3333")
                .name("강호동")
                .phoneNumber("01088776655")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        given(reactiveUserRepository.findByNameLike("동"))
                .willReturn(Flux.just(user1, user2));

        //when
        Flux<User> usersFlux = userService.searchUserByNameLike("동");

        //then
        verify(reactiveUserRepository, times(1)).findByNameLike("동");
        StepVerifier
                .create(usersFlux)
                .recordWith(ArrayList::new)
                .expectNextCount(2)
                .consumeRecordedWith(record -> {
                    var list = record.stream().map(User::getName).collect(Collectors.toList());
                    assertThat(list).contains("강호동", "신동엽");
                })
                .verifyComplete();
    }

    @Test
    void addUserTest() {
        //given
        var user1 = User.builder()
                .userId("user1")
                .password("1111")
                .name("유재석")
                .phoneNumber("01099998888")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        given(reactiveUserRepository.save(user1)).willReturn(Mono.just(user1));

        //when
        User addedUser = userService.addUser(user1).block();

        //then
        verify(reactiveUserRepository, times(1)).save(user1);
        assertThat(addedUser).isNotNull();
        assertThat(addedUser).isEqualTo(user1);
    }

    @DisplayName("유저 변경 성공")
    @Test
    void updateUserTest() {
        //given
        var user1 = User.builder()
                .userId("user1")
                .password("1111")
                .name("유재석")
                .phoneNumber("01099998888")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        when(reactiveUserRepository.findById(user1.getUserId())).thenReturn(Mono.just(user1));
        given(reactiveUserRepository.save(user1)).willReturn(Mono.just(user1));

        //when
        User updatedUser = userService.updateUser("user1", user1).block();

        //then
        verify(reactiveUserRepository, times(1)).save(user1);
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser).isEqualTo(user1);
    }

    @DisplayName("유저 삭제 성공")
    @Test
    void deleteUserTest() {
        //given
        var user1 = User.builder()
                .userId("user1")
                .password("1111")
                .name("유재석")
                .phoneNumber("01099998888")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        given(reactiveUserRepository.findById(user1.getUserId())).willReturn(Mono.just(user1));
        given(reactiveUserRepository.delete(user1)).willAnswer(invocation -> Mono.empty().then());

        //when
        Boolean result = userService.deleteUser("user1").block();

        //then
        verify(reactiveUserRepository, times(1)).delete(user1);
        assertThat(result).isNotNull();
        assertThat(result).isTrue();
    }
}