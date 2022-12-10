package com.dptablo.template.springboot;

import com.dptablo.template.springboot.kafka.consumer.LoginUserConsumer;
import com.dptablo.template.springboot.kafka.producer.LoginUserProducer;
import com.dptablo.template.springboot.model.kafka.LoginUserMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

//@SpringBootTest
//@ActiveProfiles("local")
//@Testcontainers
public class UserKafkaTest {
//    @Autowired
    private LoginUserProducer loginUserProducer;

//    @Autowired
    private LoginUserConsumer loginUserConsumer;

    private String topic = "userTopic";

//    @Test
    @DisplayName("kafka 테스트 작성중")
    void test1() throws InterruptedException {
        //given
        var message = LoginUserMessage.builder()
                .userId("tablo")
                .name("이영우")
                .loginTime(LocalDateTime.now())
                .build();

        //when
        loginUserProducer.send(topic, message);
        boolean messageConsumed = loginUserConsumer.getLatch().await(2, TimeUnit.SECONDS);

        //then
        assertThat(messageConsumed).isTrue();

        var consumerRecord = loginUserConsumer.getPayload();
        var value = consumerRecord.value();
//        assertThat(loginUserConsumer.getPayload()).contains(message);
        System.out.println("123");
    }
}
