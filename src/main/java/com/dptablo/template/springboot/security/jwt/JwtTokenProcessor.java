package com.dptablo.template.springboot.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dptablo.template.springboot.ApplicationConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProcessor {
    private final ApplicationConfiguration applicationConfiguration;

    /**
     * jwt token을 생성합니다.
     *
     * @param userDetails 사용자 정보
     * @return jwt token
     * @throws JWTCreationException jwt token 생성 실패
     */
    public String generateToken(UserDetails userDetails) throws JWTCreationException {
        Long expireSecond = 60 * applicationConfiguration.getJwtExpiryMinutes();
        return generateToken(userDetails, expireSecond);
    }

    /**
     * 만료시간(second)을 직접 지정하여 jwt token을 생성합니다.
     *
     * @param userDetails 사용자 정보
     * @param expireSecond 생성시간으로부터의 만료될 시점에 대한 second
     * @return jwt token
     * @throws JWTCreationException jwt token 생성 실패
     */
    public String generateToken(UserDetails userDetails, Long expireSecond) throws JWTCreationException {
        var algorithm = Algorithm.HMAC256(applicationConfiguration.getJwtPrivateKey());
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer(applicationConfiguration.getJwtIssUser())
                .withExpiresAt(new Date(
                        System.currentTimeMillis() + (1000 * expireSecond)
                ))
                .sign(algorithm);
    }

    /**
     * 유효한 jwt token 인지 판단합니다.
     *
     * @param token jwt token
     * @return decoded jwt 정보
     * @throws TokenExpiredException 유효하지 않은 토큰에 대한 exception
     */
    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
        return decodeToken(token);
    }

    private DecodedJWT decodeToken(String token) throws TokenExpiredException {
        var algorithm = Algorithm.HMAC256(applicationConfiguration.getJwtPrivateKey());
        var verifier = JWT.require(algorithm)
                .withIssuer(applicationConfiguration.getJwtIssUser())
                .build();
        return verifier.verify(token);
    }
}
