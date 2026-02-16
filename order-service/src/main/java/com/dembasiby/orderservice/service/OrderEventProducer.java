package com.dembasiby.orderservice.service;

import com.dembasiby.orderservice.dto.response.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "order-events";

    public void publish(OrderEvent event) {
        kafkaTemplate.send(TOPIC, event.getUserId(), event);
    }
}
