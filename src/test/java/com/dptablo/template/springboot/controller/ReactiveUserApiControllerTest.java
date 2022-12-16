package com.dptablo.template.springboot.controller;

import com.dptablo.template.springboot.configuration.SecurityConfiguration;
import com.dptablo.template.springboot.controller.mongo.reactive.ReactiveUserApiController;
import com.dptablo.template.springboot.model.dto.mongo.reactive.UserDto;
import com.dptablo.template.springboot.model.mongo.User;
import com.dptablo.template.springboot.security.jwt.JwtRequestFilter;
import com.dptablo.template.springboot.service.mongo.reactive.ReactiveUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.platform.commons.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.*;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebFluxTest(
        controllers = ReactiveUserApiController.class,
        excludeAutoConfiguration = {SecurityConfiguration.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtRequestFilter.class)
        }
)
//@WebFluxTest(ReactiveUserApiController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@Import(WebFluxControllerSecurityTestConfiguration.class)
class ReactiveUserApiControllerTest {
    private WebTestClient webTestClient;

//    @RegisterExtension
//    final RestDocumentationExtension restDocumentation =
//            new RestDocumentationExtension ("custom");

    @Qualifier("defaultReactiveUserService")
    @MockBean
    private ReactiveUserService userService;

    @BeforeEach
    void setUp(ApplicationContext applicationContext, RestDocumentationContextProvider restDocumentation) {
        this.webTestClient = WebTestClient.bindToApplicationContext(applicationContext).configureClient()
                .filter(documentationConfiguration(restDocumentation))
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

        //then
//        StepVerifier
//                .create(exchangeResult.getResponseBody())
//                .consumeNextWith(jsonString -> {
//                    try {
//                        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//
//                        var responseData = objectMapper.readValue(jsonString, UserDto.class);
//                        assertThat(responseData).isNotNull();
//                        assertThat(responseData.getUserId()).isEqualTo(user1.getUserId());
//                        assertThat(responseData.getPhoneNumber()).isEqualTo(user1.getPhoneNumber());
//                        assertThat(responseData.getName()).isEqualTo(user1.getName());
//                        assertThat(responseData.getCreateDate()).isEqualTo(user1.getCreateDate());
//                        assertThat(responseData.getUpdateDate()).isEqualTo(user1.getUpdateDate());
//                    } catch (JsonProcessingException e) {
//
//                        fail(ExceptionUtils.readStackTrace(e));
//                    }
//                })
//                .verifyComplete();


//        StepVerifier
//                .create(exchangeResult.getResponseBody())
//                .consumeNextWith(jsonString -> {
//                    try {
//                        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//
//                        var responseData = objectMapper.readValue(jsonString, UserDto.class);
//                        assertThat(responseData).isNotNull();
//                        assertThat(responseData.getUserId()).isEqualTo(user1.getUserId());
//                        assertThat(responseData.getPhoneNumber()).isEqualTo(user1.getPhoneNumber());
//                        assertThat(responseData.getName()).isEqualTo(user1.getName());
//                        assertThat(responseData.getCreateDate()).isEqualTo(user1.getCreateDate());
//                        assertThat(responseData.getUpdateDate()).isEqualTo(user1.getUpdateDate());
//                    } catch (JsonProcessingException e) {
//
//                        fail(ExceptionUtils.readStackTrace(e));
//                    }
//                })
//                .verifyComplete();
    }
}



