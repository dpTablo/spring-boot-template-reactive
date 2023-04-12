package com.dptablo.template.springboot.configuration.kafka.topic;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LoginUserTopicConfiguration {
    @Bean
    public NewTopic loginUserTopic() {
         return new NewTopic("login-user-topic", 1, (short) 1);
    }
}
