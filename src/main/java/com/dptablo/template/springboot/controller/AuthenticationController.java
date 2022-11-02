package com.dptablo.template.springboot.controller;

import com.dptablo.template.springboot.exception.ApplicationException;
import com.dptablo.template.springboot.model.dto.AuthenticationDto;
import com.dptablo.template.springboot.model.dto.LoginInfoDto;
import com.dptablo.template.springboot.service.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {
    @Qualifier("defaultJwtAuthenticationService")
    private final JwtAuthenticationService jwtAuthenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationDto> authenticate(@RequestBody LoginInfoDto loginInfo) throws ApplicationException {
        var accessToken = jwtAuthenticationService.authenticate(loginInfo.getUserId(), loginInfo.getPassword());
        var authenticationDto = AuthenticationDto.builder()
                .accessToken(accessToken)
                .build();
        return ResponseEntity.ok()
                .body(authenticationDto);
    }
}
