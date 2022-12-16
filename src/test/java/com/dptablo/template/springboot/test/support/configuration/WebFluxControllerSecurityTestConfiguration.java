package com.dptablo.template.springboot.test.support.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;

@TestConfiguration
@EnableReactiveMethodSecurity
public class WebFluxControllerSecurityTestConfiguration {
}
