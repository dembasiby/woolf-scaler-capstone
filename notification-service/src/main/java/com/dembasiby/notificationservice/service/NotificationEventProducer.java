package com.dembasiby.notificationservice.service;

import com.dembasiby.notificationservice.dto.response.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "notification-events";

    public void publish(NotificationEvent event) {
        kafkaTemplate.send(TOPIC, event.getUserId(), event);
    }
}
