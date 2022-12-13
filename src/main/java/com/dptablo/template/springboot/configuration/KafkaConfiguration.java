package com.dptablo.template.springboot.configuration;

import lombok.RequiredArgsConstructor;
import nonapi.io.github.classgraph.json.JSONSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

//@Configuration
//@RequiredArgsConstructor
//@EnableKafka
public class KafkaConfiguration {
//    private final KafkaProperties kafkaProperties;
//
//    @Bean
//    public ProducerFactory<String, Object> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfigMap());
//    }
//
//    @Bean
//    public Map<String, Object> producerConfigMap() {
//        Map<String, Object> configMap = new HashMap<>();
//        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
//        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JSONSerializer.class);
//        return configMap;
//    }
//
//    @Bean
//    public ConsumerFactory<String, Object> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(consumerConfigMap());
//    }
//
//    @Bean
//    public Map<String, Object> consumerConfigMap() {
//        var consumerProperties = kafkaProperties.getConsumer();
//
//        Map<String, Object> configMap = new HashMap<>();
//        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperties.getBootstrapServers());
//        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.getGroupId());
//        configMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerProperties.getAutoOffsetReset());
//        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        return configMap;
//    }
//
//    @Bean
//    public KafkaTemplate<String, Object> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    @Bean
//    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
//            ConsumerFactory<String, Object> consumerFactory
//    ) {
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory);
//        return factory;
//    }
}
