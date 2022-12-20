package com.dptablo.template.springboot.configuration;

import com.dptablo.template.springboot.security.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
            "/",
            "/images/**",
            "/login",
            "/api/authenticate",
            "/api/logout",
            "/api/user",

            "/api-docs",
            "/api/v3/swagger",
            "/api/v3/docs",
            "/swagger-ui.html",
            "/api-docs"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors()

                .and()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeHttpRequests()
                        .anyRequest().permitAll();

//                .authorizeHttpRequests()
//                    .requestMatchers(
//                            "/api/v3/swagger",
//                            "/api/v3/docs",
//                            "/api-docs",
//                            "/swagger-ui.html",
//                            "/api-docs").permitAll()
//                    .requestMatchers("/api/**")
//                        .hasRole("USER")
//                    .anyRequest().authenticated();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
