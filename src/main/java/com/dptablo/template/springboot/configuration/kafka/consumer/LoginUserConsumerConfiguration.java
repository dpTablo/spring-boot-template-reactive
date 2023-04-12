package com.dptablo.template.springboot.configuration.kafka.consumer;

import com.dptablo.template.springboot.model.kafka.LoginUserTopic;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor
@EnableConfigurationProperties
public class LoginUserConsumerConfiguration {
    private final KafkaProperties kafkaProperties;

    @Bean
    public ConsumerFactory<String, LoginUserTopic> loginUserConsumerFactory() {
        var stringDeserializer = new StringDeserializer();

        var loginUserJsonDeserializer = new JsonDeserializer<>(LoginUserTopic.class);
        loginUserJsonDeserializer.addTrustedPackages("com.dptablo.template.springboot.model.kafka.LoginUserTopic");

        return new DefaultKafkaConsumerFactory<>(
                loginUserConsumerConfigMap(),
                stringDeserializer,
                loginUserJsonDeserializer);
    }

    @Bean
    public Map<String, Object> loginUserConsumerConfigMap() {
        var consumerProperties = kafkaProperties.getConsumer();

        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperties.getBootstrapServers());
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.getGroupId());
        configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerProperties.getAutoOffsetReset());
        return configMap;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LoginUserTopic> loginUserListenerContainerFactory(
            ConsumerFactory<String, LoginUserTopic> consumerFactory) {

        var factory = new ConcurrentKafkaListenerContainerFactory<String, LoginUserTopic>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(retryErrorHandler());
        return factory;
    }

    private DefaultErrorHandler retryErrorHandler() {
        return new DefaultErrorHandler(new FixedBackOff(2000, 3));
    }
}
