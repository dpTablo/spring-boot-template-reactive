package com.dptablo.template.springboot.security.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dptablo.template.springboot.ApplicationConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class JwtTokenProcessorTest {
    @InjectMocks
    private JwtTokenProcessor jwtTokenProcessor;

    @Mock
    private ApplicationConfiguration applicationConfiguration;

    @BeforeEach
    void beforeEach() {
        given(applicationConfiguration.getJwtIssUser()).willReturn("dptablo");
        given(applicationConfiguration.getJwtPrivateKey()).willReturn("dptablo:jwt:privateToken");
    }

    @DisplayName("jwt token 생성 - 기본 테스트")
    @Test
    void generateToken() {
        //given
        given(applicationConfiguration.getJwtExpiryMinutes()).willReturn(10L);

        UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getUsername()).willReturn("user1");

        //when
        String token = jwtTokenProcessor.generateToken(userDetails);

        //then
        assertThat(token).isNotNull();
        assertThat(token.length()).isGreaterThanOrEqualTo(1);
    }

    @DisplayName("jwt token 생성 - expireSecond 지정")
    @Test
    void generateToken_expireSecond() {
        //given
        UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getUsername()).willReturn("user1");

        //when
        String token = jwtTokenProcessor.generateToken(userDetails, 3000L);

        //then
        assertThat(token).isNotNull();
        assertThat(token.length()).isGreaterThanOrEqualTo(1);
    }

    @DisplayName("jwt token verify - 기본 테스트")
    @Test
    void verifyToken() {
        //given
        given(applicationConfiguration.getJwtExpiryMinutes()).willReturn(10L);

        UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getUsername()).willReturn("user1");

        String token = jwtTokenProcessor.generateToken(userDetails);

        //when
        DecodedJWT decodedJWT = jwtTokenProcessor.verifyToken(token);

        //then
        assertThat(decodedJWT).isNotNull();
        assertThat(decodedJWT.getToken()).isEqualTo(token);
    }

    @DisplayName("jwt token verify - 유효하지 토큰 검증")
    @Test
    void verifyToken_invalid() {
        //given
        UserDetails userDetails = mock(UserDetails.class);
        given(userDetails.getUsername()).willReturn("user1");

        String token = jwtTokenProcessor.generateToken(userDetails, 1L);

        //when
        Throwable thrown = catchThrowable(() -> Thread.sleep(2000));
        assertThat(thrown).isNull();

        assertThatThrownBy(() -> jwtTokenProcessor.verifyToken(token))
                .isInstanceOf(TokenExpiredException.class)
                .hasMessageContaining("expired");
    }
}