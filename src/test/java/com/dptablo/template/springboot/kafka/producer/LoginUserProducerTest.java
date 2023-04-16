package com.dptablo.template.springboot.kafka.producer;

import com.dptablo.template.springboot.configuration.KafkaConfiguration;
import com.dptablo.template.springboot.configuration.kafka.producer.LoginUserProducerConfiguration;
import com.dptablo.template.springboot.configuration.kafka.topic.LoginUserTopicConfiguration;
import com.dptablo.template.springboot.model.kafka.LoginUserTopic;
import com.dptablo.template.springboot.test.support.TestContainersKafkaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.*;

@TestContainersKafkaTest
@ActiveProfiles("tc")
@ContextConfiguration(classes = {
        KafkaProperties.class,
        KafkaConfiguration.class,
        LoginUserProducerConfiguration.class,
        LoginUserTopicConfiguration.class,
        LoginUserProducer.class
})
@EnableAutoConfiguration
class LoginUserProducerTest {
    @Qualifier("loginUserProducer")
    @Autowired
    private LoginUserProducer producer;

    @DisplayName("메세지 송신 성공")
    @Test
    void test() throws ExecutionException, InterruptedException {
        // given
        var loginUserTopic = LoginUserTopic.builder()
                .userId("user1")
                .name("사용자1")
                .loginTime(LocalDateTime.now())
                .build();

        // when
        var completableFuture = producer.sendMessage(loginUserTopic);
        var sendResult = completableFuture.get();

        // then
        assertThat(sendResult).isNotNull();

        var producerRecord = sendResult.getProducerRecord();
        assertThat(producerRecord).isNotNull();
        assertThat(producerRecord.topic()).isEqualTo(LoginUserTopic.TOPIC_NAME);

        var loginUserTopicInSendResult = producerRecord.value();
        assertThat(loginUserTopicInSendResult).isEqualTo(loginUserTopic);
    }
}