package com.dptablo.template.springboot.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
@RequiredArgsConstructor
public class UserKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, String payload) {
        kafkaTemplate.send(topic, payload);
    }
}
