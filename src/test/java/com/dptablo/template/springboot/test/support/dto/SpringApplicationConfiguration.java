package com.dptablo.template.springboot.test.support.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpringApplicationConfiguration {
    private SpringConfiguration spring;
}