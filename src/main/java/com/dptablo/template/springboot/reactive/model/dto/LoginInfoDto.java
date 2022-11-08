package com.dptablo.template.springboot.reactive.model.dto;

import com.dptablo.template.springboot.reactive.model.HttpDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginInfoDto implements HttpDto {
    private String userId;
    private String password;
}
