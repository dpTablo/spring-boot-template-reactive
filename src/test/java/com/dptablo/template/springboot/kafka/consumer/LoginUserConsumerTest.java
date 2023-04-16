package com.dptablo.template.springboot.kafka.consumer;

import com.dptablo.template.springboot.configuration.KafkaConfiguration;
import com.dptablo.template.springboot.configuration.kafka.consumer.LoginUserConsumerConfiguration;
import com.dptablo.template.springboot.configuration.kafka.topic.LoginUserTopicConfiguration;
import com.dptablo.template.springboot.model.kafka.LoginUserTopic;
import com.dptablo.template.springboot.test.support.TestContainersKafkaTest;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@TestContainersKafkaTest
@ActiveProfiles("tc")
@ContextConfiguration(classes = {
        KafkaProperties.class,
        KafkaConfiguration.class,
        LoginUserConsumerConfiguration.class,
        LoginUserTopicConfiguration.class,
        LoginUserConsumer.class
})
@EnableAutoConfiguration
class LoginUserConsumerTest {
    @Qualifier("loginUserConsumer")
    @Autowired
    private LoginUserConsumer consumer;

    @Autowired
    private KafkaProperties kafkaProperties;

    private KafkaTemplate<String, LoginUserTopic> kafkaTemplate;

    @BeforeEach
    public void beforeEach() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        var producerFactory = new DefaultKafkaProducerFactory<String, LoginUserTopic>(configMap);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    @DisplayName("메세지 수신 성공")
    @Test
    void test() throws ExecutionException, InterruptedException {
        // given

        // when
        var completableFuture = kafkaTemplate.send(LoginUserTopic.TOPIC_NAME, LoginUserTopic.builder()
                .userId("user1")
                .name("사용자1")
                .loginTime(LocalDateTime.now())
                .build());
        var sendResult = completableFuture.get();

        var messageConsumed = consumer.getLatch().await(5, TimeUnit.SECONDS);

        // then
        assertThat(sendResult).isNotNull();
        assertThat(messageConsumed).isTrue();
        assertThat(consumer.getPayload());
    }
}