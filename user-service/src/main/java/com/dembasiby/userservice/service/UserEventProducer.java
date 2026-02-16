package com.dembasiby.userservice.service;

import com.dembasiby.userservice.dto.response.event.PasswordResetEvent;
import com.dembasiby.userservice.dto.response.event.UserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String USER_EVENTS_TOPIC = "user-events";
    private static final String PASSWORD_RESET_TOPIC = "password-reset-events";

    public void publishUserEvent(UserEvent event) {
        kafkaTemplate.send(USER_EVENTS_TOPIC, event.getUserId().toString(), event);
    }

    public void publishPasswordResetEvent(PasswordResetEvent event) {
        kafkaTemplate.send(PASSWORD_RESET_TOPIC, event.getUserId().toString(), event);
    }
}
