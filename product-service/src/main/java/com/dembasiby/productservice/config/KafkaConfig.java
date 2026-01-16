package com.dembasiby.productservice.config;

import com.dembasiby.productservice.document.ProductDocument;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductDocument>
    kafkaListenerContainerFactory(ConsumerFactory<String, ProductDocument> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, ProductDocument> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}







