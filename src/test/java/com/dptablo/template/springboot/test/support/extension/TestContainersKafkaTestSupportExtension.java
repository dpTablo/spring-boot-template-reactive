package com.dptablo.template.springboot.test.support.extension;

import com.dptablo.template.springboot.test.support.dto.SpringApplicationConfiguration;
import com.dptablo.template.springboot.test.support.settings.TestContainersKafkaSettins;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.kafka.streams.Topology;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.nio.file.Paths;

@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-tc.yml")
public class TestContainersKafkaTestSupportExtension implements
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private KafkaContainer kafkaContainer;

    private SpringApplicationConfiguration springApplicationConfiguration;

    public TestContainersKafkaTestSupportExtension() {
        initSpringApplicationConfiguration();
    }

    private void initSpringApplicationConfiguration() {
        try {
            var applicationTcYmlFilePath = Paths.get(ClassLoader.getSystemResource("application-tc.yml").toURI());
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory())
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            springApplicationConfiguration = objectMapper.readValue(applicationTcYmlFilePath.toFile(), SpringApplicationConfiguration.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        kafkaContainer = new KafkaContainer(DockerImageName.parse(TestContainersKafkaSettins.KAFKA_IMAGES_TAG));
        kafkaContainer.start();

        setupSpringApplicationConfiguration();
    }

    private void setupSpringApplicationConfiguration() {
        //configuration
        /*
                kafka:
                    producer:
                        bootstrap-servers: localhost:9092
                    consumer:
                        bootstrap-servers: localhost:9092
                        auto-offset-reset: earliest
                        group-id: tablo-kafka-group
                        enable-auto-commit: true
                    listener:
                        ack-mode: record
                    bootstrap-servers: localhost:9092
         */
        System.setProperty("spring.kafka.producer.bootstrap-servers", kafkaContainer.getBootstrapServers());

        System.setProperty("spring.kafka.consumer.bootstrap-servers", kafkaContainer.getBootstrapServers());
        System.setProperty("spring.kafka.consumer.auto-offset-reset", Topology.AutoOffsetReset.EARLIEST.toString());
        System.setProperty("spring.kafka.consumer.group-id", "test-kafka-groupId");
        System.setProperty("spring.kafka.consumer.enable-auto-commit", "true");

        System.setProperty("spring.kafka.listener.ack-mode", ContainerProperties.AckMode.RECORD.toString());
        System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {

    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {

    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if(kafkaContainer != null && kafkaContainer.isRunning()) {
            kafkaContainer.stop();
            kafkaContainer.stop();
        }
    }
}
