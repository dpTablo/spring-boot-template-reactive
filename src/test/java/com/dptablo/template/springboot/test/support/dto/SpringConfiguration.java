package com.dptablo.template.springboot.test.support.dto;

import com.dptablo.template.springboot.test.support.dto.FlywayConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpringConfiguration {
    private FlywayConfiguration flyway;
}
