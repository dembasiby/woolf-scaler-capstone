package com.dembasiby.cartservice.service;

import com.dembasiby.cartservice.dto.response.event.CartEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "cart-events";

    public void publish(CartEvent event) {
        kafkaTemplate.send(TOPIC, event.getUserId(), event);
    }
}
