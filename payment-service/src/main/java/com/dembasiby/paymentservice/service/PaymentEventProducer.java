package com.dembasiby.paymentservice.service;

import com.dembasiby.paymentservice.dto.response.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "payment-events";

    public void publish(PaymentEvent event) {
        kafkaTemplate.send(TOPIC, event.getUserId(), event);
    }
}
