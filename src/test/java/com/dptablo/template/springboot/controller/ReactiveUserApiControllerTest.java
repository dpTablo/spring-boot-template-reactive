package com.dptablo.template.springboot.controller;

import com.dptablo.template.springboot.configuration.SecurityConfiguration;
import com.dptablo.template.springboot.controller.mongo.reactive.ReactiveUserApiController;
import com.dptablo.template.springboot.model.dto.mongo.reactive.UpdateUserDto;
import com.dptablo.template.springboot.model.dto.mongo.reactive.UserDto;
import com.dptablo.template.springboot.model.mongo.reactive.User;
import com.dptablo.template.springboot.security.jwt.JwtRequestFilter;
import com.dptablo.template.springboot.service.mongo.reactive.ReactiveUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebFluxTest(
        controllers = ReactiveUserApiController.class,
        excludeAutoConfiguration = {SecurityConfiguration.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtRequestFilter.class)
        }
)
class ReactiveUserApiControllerTest {
    private WebTestClient webTestClient;

    @Qualifier("defaultReactiveUserService")
    @MockBean
    private ReactiveUserService userService;

    private FieldDescriptor[] userDtoFieldDescriptors = new FieldDescriptor[] {
            fieldWithPath("userId").description("유저 id"),
            fieldWithPath("name").description("유져 이름"),
            fieldWithPath("phoneNumber").description("휴대폰번호"),
            fieldWithPath("createDate").description("등록일자"),
            fieldWithPath("updateDate").description("변경일자")
    };

    private FieldDescriptor[] updateUserDtoFieldDescriptors = new FieldDescriptor[] {
            fieldWithPath("userId").description("유저 id"),
            fieldWithPath("password").description("비밀번호"),
            fieldWithPath("name").description("유져 이름"),
            fieldWithPath("phoneNumber").description("휴대폰번호"),
            fieldWithPath("createDate").description("등록일자"),
            fieldWithPath("updateDate").description("변경일자")
    };

    private FieldDescriptor[] responseDtoFieldDescriptors = new FieldDescriptor[] {
            fieldWithPath("code").description("결과 코드"),
            fieldWithPath("message").description("서버 메세지"),
            subsectionWithPath("data").description("데이터")
    };

    @BeforeEach
    void setUp(ApplicationContext applicationContext, RestDocumentationContextProvider restDocumentation) {
        this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext).configureClient()
                .filter(WebTestClientRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    @DisplayName("특정 유저 정보 조회")
    @Test
    @WithMockUser
    void getUserTest() throws Exception {
        //given
        var user1 = User.builder()
                .userId("user1")
                .password("1234")
                .phoneNumber("01011112222")
                .name("유저1")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
        given(userService.getUser("user1")).willReturn(Mono.just(user1));

        //when & then
        webTestClient.get().uri("/api/v3/user/{userId}", "user1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(
                        document("v3-user-getUser",
                                pathParameters(
                                        parameterWithName("userId").description("사용자 id")
                                ),
                                responseFields(
                                        fieldWithPath("userId").description("유저 id"),
                                        fieldWithPath("name").description("유져 이름"),
                                        fieldWithPath("phoneNumber").description("휴대폰번호"),
                                        fieldWithPath("createDate").description("등록일자"),
                                        fieldWithPath("updateDate").description("변경일자")
                                )
                        ))
                .jsonPath("name").isEqualTo(user1.getName());
    }

    @DisplayName("유저명으로 검색 조회")
    @Test
    @WithMockUser
    public void searchUserByNameLikeTest() throws JsonProcessingException {
        //given
        var user1 = User.builder()
                .userId("user2")
                .password("1234")
                .phoneNumber("01011112222")
                .name("이민석")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        var user2 = User.builder()
                .userId("user3")
                .password("1234")
                .phoneNumber("01011112222")
                .name("유재석")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        given(userService.searchUserByNameLike("석")).willReturn(Flux.just(user1, user2));

        //when
        var jsonData = webTestClient.get().uri(uriBuilder -> uriBuilder
                        .path("/api/v3/user/search")
                        .queryParam("name", "석")
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(
                        document("v3-user-searchUserByNameLike",
                                queryParameters(
                                        parameterWithName("name").description("검색할 유저명")
                                ),
                                responseFields(
                                        fieldWithPath("[]").description("유저 리스트")
                                ).andWithPrefix("[].", userDtoFieldDescriptors)
                        ))
                .returnResult().getResponseBody();
        var jsonStringValue = new String(jsonData);

        //then
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        var resultList = Arrays.asList(objectMapper.readValue(jsonStringValue, UserDto[].class));

        assertThat(resultList).isNotNull();
        assertThat(resultList.size()).isEqualTo(2);

        var userNames = resultList.stream()
                .map(item -> item.getName())
                .collect(Collectors.toList());
        assertThat(userNames.contains("유재석")).isTrue();
        assertThat(userNames.contains("이민석")).isTrue();
    }

    @DisplayName("유저 등록")
    @Test
    @WithMockUser
    public void addUserTest() throws JsonProcessingException {
        //given
        var user1 = UpdateUserDto.builder()
                .userId("user1")
                .password("1234")
                .phoneNumber("01011112222")
                .name("사용자1")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        var userJsonString = objectMapper.writeValueAsString(user1);

        given(userService.addUser(any(User.class)))
                .willAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        //when & then
        webTestClient
                .mutateWith(csrf())
                .post().uri("/api/v3/user")
                .headers(httpHeaders -> httpHeaders
                        .set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                )
                .body(
                        BodyInserters.fromValue(userJsonString)
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(
                        document("v3-user-addUser",
                                requestFields(
                                        updateUserDtoFieldDescriptors
                                ),
                                responseFields(
                                        responseDtoFieldDescriptors
                                ).andWithPrefix("data.", userDtoFieldDescriptors)
                        )
                )
                .jsonPath("data.userId").isEqualTo(user1.getUserId());

        //then
        verify(userService, times(1)).addUser(any(User.class));
    }

    @DisplayName("유저 변경")
    @Test
    @WithMockUser
    public void updateUserTest() throws JsonProcessingException {
        //given
        var user1 = UpdateUserDto.builder()
                .userId("user1")
                .password("1234")
                .phoneNumber("01011112222")
                .name("사용자1")
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        var userJsonString = objectMapper.writeValueAsString(user1);

        given(userService.updateUser(any(String.class), any(User.class)))
                .willAnswer(invocation -> {
                    var user = ((User) invocation.getArgument(1));
                    user.setUserId(invocation.getArgument(0));
                    return Mono.just(user);
                });

        //when & then
        webTestClient
                .mutateWith(csrf())
                .put().uri("/api/v3/user")
                .headers(httpHeaders -> httpHeaders
                        .set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                )
                .body(
                        BodyInserters.fromValue(userJsonString)
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(
                        document("v3-user-updateUser",
                                requestFields(
                                        updateUserDtoFieldDescriptors
                                ),
                                responseFields(
                                        responseDtoFieldDescriptors
                                ).andWithPrefix("data.", userDtoFieldDescriptors)
                        )
                )
                .jsonPath("data.userId").isEqualTo(user1.getUserId())
                .jsonPath("data.name").isEqualTo(user1.getName())
                .jsonPath("data.phoneNumber").isEqualTo(user1.getPhoneNumber())
                .jsonPath("data.createDate").hasJsonPath()
                .jsonPath("data.updateDate").hasJsonPath();

        //then
        verify(userService, times(1))
                .updateUser(any(String.class), any(User.class));
    }

    @DisplayName("유저 삭제")
    @Test
    @WithMockUser
    public void deleteUser() {
        //given
        String TEST_USER_ID = "user1";
        given(userService.deleteUser(TEST_USER_ID))
                .willAnswer(invocation ->
                        Mono.just(invocation.getArgument(0).equals(TEST_USER_ID)));

        //when
        WebTestClient.BodyContentSpec bodyContentSpec = webTestClient
                .mutateWith(csrf())
                .delete().uri("/api/v3/user/{userId}", TEST_USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(
                        document("v3-user-deleteUser",
                                pathParameters(
                                        parameterWithName("userId").description("사용자 id")
                                )
                        )
                )
                .jsonPath("data").isEqualTo(true);

        //then
        verify(userService, times(1)).deleteUser(TEST_USER_ID);
    }
}



