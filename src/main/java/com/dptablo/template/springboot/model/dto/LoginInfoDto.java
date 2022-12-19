package com.dptablo.template.springboot.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginInfoDto {
    private String userId;
    private String password;
}
