package com.dptablo.template.springboot.kafka.consumer;

import com.dptablo.template.springboot.model.kafka.LoginUserMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginUserConsumer {
    private final int COUNT_DOWN = 1;

    @Getter
    private CountDownLatch latch = new CountDownLatch(COUNT_DOWN);

    @Getter
    private LoginUserMessage message;

    @KafkaListener(topics = "userTopic", groupId = "tablo-kafka-group")
    public void receive(LoginUserMessage message) {
        this.message = message;
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(COUNT_DOWN);
    }
}
