// package com.activity.config;

// import java.util.HashMap;
// import java.util.Map;

// import org.apache.kafka.clients.admin.AdminClientConfig;
// import org.apache.kafka.clients.admin.NewTopic;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.kafka.core.DefaultKafkaProducerFactory;
// import org.springframework.kafka.core.KafkaAdmin;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.core.ProducerFactory;
// import org.springframework.kafka.support.serializer.JsonSerializer;

// @Configuration
// public class KafkaConfig {

// @Value("${spring.kafka.bootstrap-servers}")
// private String bootstrapServers;

// @Value("${spring.kafka.consumer.group-id}")
// private String groupId;

// @Bean
// public KafkaAdmin kafkaAdmin() {
// final Map<String, Object> configs = new HashMap<>();
// configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
// return new KafkaAdmin(configs);
// }

// @Bean
// public NewTopic activityTopic() {
// return new NewTopic("activity-events", 1, (short) 1);
// }

// @Bean
// public ProducerFactory<String, Object> producerFactory() {
// final Map<String, Object> configProps = new HashMap<>();
// configProps.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
// bootstrapServers);
// configProps.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
// JsonSerializer.class);
// configProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
// JsonSerializer.class);
// return new DefaultKafkaProducerFactory<>(configProps);
// }

// @Bean
// public KafkaTemplate<String, Object> kafkaTemplate() {
// return new KafkaTemplate<>(producerFactory());
// }
// }
