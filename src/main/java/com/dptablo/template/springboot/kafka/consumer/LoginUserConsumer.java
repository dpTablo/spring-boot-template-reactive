package com.dptablo.template.springboot.kafka.consumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

//@Component
@Slf4j
@RequiredArgsConstructor
public class LoginUserConsumer {
    @Getter
    private CountDownLatch latch = new CountDownLatch(1);

    @Getter
    private ConsumerRecord payload;

    @KafkaListener(topics = "userTopic", groupId = "tablo-kafka-group")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        payload = consumerRecord;
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}
