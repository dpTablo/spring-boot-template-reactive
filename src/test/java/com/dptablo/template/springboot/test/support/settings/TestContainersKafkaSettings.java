package com.dptablo.template.springboot.test.support.settings;

public class TestContainersKafkaSettings {
    public static final String KAFKA_IMAGES_TAG = "confluentinc/cp-kafka:7.2.3";
    public static final String ZOOKEEPER_IMAGES_TAG = "confluentinc/cp-zookeeper:7.2.3";
    public static final int KAFKA_MAIN_PORT = 9092;
    public static final int KAFKA_SUB_PORT = 9093;
    public static final int ZOOKEEPER_PORT = 2181;

    public static final String CONSUMER_GROUP_ID = "kafka-test-group";
}
