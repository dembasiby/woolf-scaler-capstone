package com.dembasiby.productservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic productCreatedTopic() {
        return TopicBuilder.name("product-created")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productUpdatedTopic() {
        return TopicBuilder.name("product-updated")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productDeletedTopic() {
        return TopicBuilder.name("product-deleted")
                .partitions(1)
                .replicas(1)
                .build();
    }
}