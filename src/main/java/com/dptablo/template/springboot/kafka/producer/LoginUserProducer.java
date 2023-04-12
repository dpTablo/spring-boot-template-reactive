package com.dptablo.template.springboot.kafka.producer;

import com.dptablo.template.springboot.model.kafka.LoginUserTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginUserProducer {
    @Qualifier("loginUserKafkaTemplate")
    private final KafkaTemplate<String, LoginUserTopic> kafkaTemplate;

    public CompletableFuture<SendResult<String, LoginUserTopic>> sendMessage(LoginUserTopic payload) {
        return kafkaTemplate.send(LoginUserTopic.TOPIC_NAME, payload);
    }
}
