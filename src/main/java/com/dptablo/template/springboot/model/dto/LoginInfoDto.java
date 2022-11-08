package com.dptablo.template.springboot.model.dto;

import com.dptablo.template.springboot.model.HttpDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginInfoDto implements HttpDto {
    private String userId;
    private String password;
}
