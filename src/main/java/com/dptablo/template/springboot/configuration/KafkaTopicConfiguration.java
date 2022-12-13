package com.dptablo.template.springboot.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

//@Configuration
//@RequiredArgsConstructor
public class KafkaTopicConfiguration {
//    private final KafkaProperties kafkaProperties;
//
//    @Bean
//    public KafkaAdmin kafkaAdmin() {
//        Map<String, Object> configMap = new HashMap<>();
//        configMap.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
//        return new KafkaAdmin(configMap);
//    }
//
//    @Bean
//    public NewTopic userTopic() {
//         return new NewTopic("userTopic", 1, (short) 1);
//    }
}
