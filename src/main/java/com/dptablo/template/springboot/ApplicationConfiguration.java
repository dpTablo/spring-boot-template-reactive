package com.dptablo.template.springboot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application-configuration.yml")
@Getter
@Slf4j
public class ApplicationConfiguration {
    @Value("${jwt.issuser:dptablo}")
    private String jwtIssUser;

    @Value("${jwt.privateKey:dptablo_springboot}")
    private String jwtPrivateKey;

    @Value("${jwt.expiryMinutes:30}")
    private long jwtExpiryMinutes;

    @Value("${jwt.header:Authorization}")
    private String header;
}
