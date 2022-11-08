package com.dptablo.template.springboot.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
@EntityScan(basePackages = {"com.dptablo.template.springboot.reactive.model.entity"})
@EnableJpaRepositories(basePackages = {"com.dptablo.template.springboot.repository.jpa"})
public class JpaConfiguration {
    @Bean(name="entityManagerFactory")
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        return sessionFactory;
    }
}
