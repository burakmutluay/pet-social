package com.bmutlay.postservice.config;

import com.bmutlay.postservice.model.PostCreatedEvent;
import com.bmutlay.postservice.model.UserFollowEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaProducerConfig {

    private Map<String, Object> producerConfig() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafkaserver:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProps;
    }

    @Bean
    public ProducerFactory<UUID, PostCreatedEvent> postEventProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean ProducerFactory<UUID, UserFollowEvent> userFollowEventProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<UUID, PostCreatedEvent> kafkaTemplate() {
        return new KafkaTemplate<>(postEventProducerFactory());
    }

    @Bean
    public KafkaTemplate<UUID, UserFollowEvent> userFollowEventKafkaTemplate() {
        return new KafkaTemplate<>(userFollowEventProducerFactory());
    }
}
