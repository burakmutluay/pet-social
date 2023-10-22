package com.bmutlay.postservice.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafkaserver:9092");
        return new KafkaAdmin(props);
    }
    @Bean
    public NewTopic postTopic() {
        return TopicBuilder.name("post-topic")
                .partitions(2)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic postFeedFollowTopic() {
        return TopicBuilder.name("post-feed-follow-topic")
                .partitions(2)
                .replicas(1)
                .build();
    }

}
