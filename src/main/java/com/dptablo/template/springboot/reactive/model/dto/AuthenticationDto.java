package com.dptablo.template.springboot.reactive.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthenticationDto {
    private String accessToken;
}
