package com.dptablo.template.springboot.configuration.kafka.producer;

import com.dptablo.template.springboot.model.kafka.LoginUserTopic;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
@EnableConfigurationProperties
public class LoginUserProducerConfiguration {
    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaTemplate<String, LoginUserTopic> loginUserKafkaTemplate() {
        return new KafkaTemplate<>(loginUserProducerFactory());
    }

    @Bean
    public ProducerFactory<String, LoginUserTopic> loginUserProducerFactory() {
        return new DefaultKafkaProducerFactory<>(loginUserProducerConfigMap());
    }

    @Bean
    public Map<String, Object> loginUserProducerConfigMap() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configMap;
    }
}
