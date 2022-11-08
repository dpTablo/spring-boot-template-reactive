package com.dptablo.template.springboot.reactive.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"com.dptablo.template.springboot.reactive.model.entity"})
@EnableJpaRepositories(basePackages = {"com.dptablo.template.springboot.reactive.repository.jpa"})
public class JpaConfiguration {
}
