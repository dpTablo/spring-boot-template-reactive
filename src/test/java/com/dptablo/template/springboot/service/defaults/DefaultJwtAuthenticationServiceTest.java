package com.dptablo.template.springboot.service.defaults;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.dptablo.template.springboot.exception.ApplicationException;
import com.dptablo.template.springboot.model.entity.User;
import com.dptablo.template.springboot.repository.UserRepository;
import com.dptablo.template.springboot.security.jwt.JwtTokenProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class DefaultJwtAuthenticationServiceTest {
    @InjectMocks
    private DefaultJwtAuthenticationService jwtAuthenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProcessor jwtTokenProcessor;

    @Test
    void signUp() {
        fail("not implement.");
    }

    @DisplayName("authenticate - user1 // 1234")
    @Test
    void authenticate() throws ApplicationException {
        //given
        final var userId = "user1";
        final var password = "1234";
        final var generatedJwtToken = "skljf2ucoiu3cn8ru2cirniu49p8ceoi";

        //given - User
        var user = mock(User.class);
        given(user.getUserId()).willReturn(userId);
        given(user.getPassword()).willReturn(password);
        given(userRepository.findById(userId)).willReturn(Optional.ofNullable(user));

        //given - passwordEncoder
        given(passwordEncoder.matches(password, user.getPassword())).willReturn(true);

        //given - jwtTokenProcessor
        given(jwtTokenProcessor.generateToken(any())).willReturn(generatedJwtToken);

        //when
        var token = jwtAuthenticationService.authenticate(userId, password);

        //then
        assertThat(token).isNotNull();
        assertThat(token).isEqualTo(generatedJwtToken);
    }

    @DisplayName("verifyToken - 기본 테스트")
    @Test
    void verifyToken() {
        //given
        final var jwtToken = "akkldsfjklo32ox929x3";

        var decodedJWT = mock(DecodedJWT.class);
        given(decodedJWT.getToken()).willReturn(jwtToken);
        given(jwtTokenProcessor.verifyToken(jwtToken)).willReturn(decodedJWT);

        //when
        boolean result = jwtAuthenticationService.verifyToken(jwtToken);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void getAuthentication() {
        fail("not implement.");
    }
}