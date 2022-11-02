package com.dptablo.template.springboot.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"com.dptablo.template.springboot.model.entity"})
@EnableJpaRepositories(basePackages = {"com.dptablo.template.springboot.repository"})
public class JpaConfiguration {
}
