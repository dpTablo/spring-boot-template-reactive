package com.dptablo.template.springboot.test.support;

import com.dptablo.template.springboot.test.support.settings.TestContainersKafkaSettings;
import org.junit.jupiter.api.extension.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

@EnableConfigurationProperties
@TestPropertySource(locations = "classpath:application-tc.yml")
public class KafkaTestSupportExtension implements
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private KafkaContainer kafkaContainer;
    private GenericContainer<?> zookeeperContainer;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        var network = Network.newNetwork();
        zookeeperContainer =
                new GenericContainer(DockerImageName.parse(TestContainersKafkaSettings.ZOOKEEPER_IMAGES_TAG))
                        .withExposedPorts(TestContainersKafkaSettings.ZOOKEEPER_PORT)
                        .withEnv("ZOOKEEPER_CLIENT_PORT", String.valueOf(TestContainersKafkaSettings.ZOOKEEPER_PORT))
                        .withNetwork(network)
                        .withNetworkAliases("zookeeper");

        kafkaContainer = new KafkaContainer(DockerImageName.parse(TestContainersKafkaSettings.KAFKA_IMAGES_TAG))
                .withExposedPorts(TestContainersKafkaSettings.KAFKA_MAIN_PORT)
                .withExposedPorts(TestContainersKafkaSettings.KAFKA_SUB_PORT)
                .withExternalZookeeper("zookeeper:%s".formatted(TestContainersKafkaSettings.ZOOKEEPER_PORT))
                .withNetwork(network);

        Startables.deepStart(zookeeperContainer, kafkaContainer).join();

        setupSpringApplicationConfiguration();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception { }

    @Override
    public void afterEach(ExtensionContext context) throws Exception { }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (kafkaContainer != null && kafkaContainer.isRunning()) {
            kafkaContainer.stop();
            kafkaContainer = null;
        }
        if (zookeeperContainer != null && zookeeperContainer.isRunning()) {
            zookeeperContainer.stop();
            zookeeperContainer = null;
        }
    }

    private void setupSpringApplicationConfiguration() {
        System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
        System.setProperty("spring.kafka.producer.bootstrap-servers", kafkaContainer.getBootstrapServers());
        System.setProperty("spring.kafka.consumer.bootstrap-servers", kafkaContainer.getBootstrapServers());

        System.setProperty("spring.kafka.consumer.auto-offset-reset", "earliest");
        System.setProperty("spring.kafka.consumer.group-id", TestContainersKafkaSettings.CONSUMER_GROUP_ID);
        System.setProperty("spring.kafka.consumer.enable-auto-commit", "false");
        System.setProperty("spring.kafka.listener.ack-mode", "manual");
    }
}
