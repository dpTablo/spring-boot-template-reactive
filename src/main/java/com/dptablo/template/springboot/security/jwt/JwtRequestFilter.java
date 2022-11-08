package com.dptablo.template.springboot.security.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.dptablo.template.springboot.exception.ApplicationErrorCode;
import com.dptablo.template.springboot.model.ResponseDto;
import com.dptablo.template.springboot.service.JwtAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    @Qualifier("defaultJwtAuthenticationService")
    private final JwtAuthenticationService jwtAuthenticationService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        var accessToken = getAccessTokenInRequest(request);
        try {
            if(accessToken != null && jwtAuthenticationService.verifyToken(accessToken)) {
                Authentication authentication = jwtAuthenticationService.getAuthentication(accessToken)
                        .orElseThrow(ServletException::new);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch(TokenExpiredException e) {
            var responseDto = ResponseDto.builder()
                    .code(ApplicationErrorCode.AUTHENTICATION_EXPIRED.getErrorCode())
                    .message(ApplicationErrorCode.AUTHENTICATION_EXPIRED.getDescription())
                    .build();

            response.setStatus(ApplicationErrorCode.AUTHENTICATION_EXPIRED.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(response.getWriter(), responseDto);
        }
    }

    private String getAccessTokenInRequest(HttpServletRequest request) {
        var bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
