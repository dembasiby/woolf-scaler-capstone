package com.dembasiby.cartservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic cartEventsTopic() {
        return TopicBuilder.name("cart-events")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic productUpdatesTopic() {
        return TopicBuilder.name("product-updates")
                .partitions(1)
                .replicas(1)
                .build();
    }
}

