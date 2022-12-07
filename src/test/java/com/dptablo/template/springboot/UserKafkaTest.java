package com.dptablo.template.springboot;

import com.dptablo.template.springboot.configuration.*;
import com.dptablo.template.springboot.kafka.consumer.LoginUserConsumer;
import com.dptablo.template.springboot.kafka.producer.LoginUserProducer;
import com.dptablo.template.springboot.model.kafka.LoginUserMessage;
import com.dptablo.template.springboot.test.support.extension.TestContainersKafkaTestSupportExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("tc")
@Testcontainers
@ExtendWith(SpringExtension.class)
@ExtendWith(TestContainersKafkaTestSupportExtension.class)
//@ContextConfiguration(classes = {
//        KafkaConfiguration.class,
//        KafkaTopicConfiguration.class,
//        LoginUserProducer.class,
//        LoginUserConsumer.class,
//        PostgresR2dbcConfiguration.class
//})
//@EnableAutoConfiguration(exclude = PostgresR2dbcConfiguration.class)
public class UserKafkaTest {
    @Autowired
    private LoginUserProducer loginUserProducer;

    @Autowired
    private LoginUserConsumer loginUserConsumer;

    private String topic = "userTopic";

    @Test
    @DisplayName("LoginUserMessage 메세지 처리")
    void loginUserMessageTest() throws InterruptedException {
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

        var receivedMessage = loginUserConsumer.getMessage();
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage).isEqualTo(message);
    }
}
