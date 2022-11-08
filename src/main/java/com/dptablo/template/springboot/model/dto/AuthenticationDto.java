package com.dptablo.template.springboot.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthenticationDto {
    private String accessToken;
}
