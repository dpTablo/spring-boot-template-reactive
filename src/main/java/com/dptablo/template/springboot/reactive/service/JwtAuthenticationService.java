package com.dptablo.template.springboot.reactive.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dptablo.template.springboot.reactive.exception.ApplicationException;
import com.dptablo.template.springboot.reactive.model.entity.User;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface JwtAuthenticationService {
    User signUp(String userId, String password);
    String authenticate(String userId, String password) throws ApplicationException;
    boolean verifyToken(String token) throws JWTVerificationException;
    Optional<Authentication> getAuthentication(String token);
}
