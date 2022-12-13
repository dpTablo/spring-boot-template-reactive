package com.dptablo.template.springboot.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

//@Component
@Slf4j
@RequiredArgsConstructor
public class UserKafkaConsumer {
    private CountDownLatch latch = new CountDownLatch(1);
    private String payload;

    @KafkaListener(topics = "userTopic")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        payload = consumerRecord.toString();
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}
