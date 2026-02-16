package com.dembasiby.notificationservice.service;

import com.dembasiby.notificationservice.dto.request.SendNotificationRequest;
import com.dembasiby.notificationservice.model.NotificationChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void consume(Map<String, Object> event) {
        String type = (String) event.get("type");
        log.info("Received order event: {}", type);

        String userId = (String) event.get("userId");
        Long orderId = ((Number) event.get("orderId")).longValue();
        String email = event.get("email") != null ? (String) event.get("email") : userId;

        String subject;
        String body;

        switch (type) {
            case "ORDER_CONFIRMED" -> {
                subject = "Order Confirmed - #" + orderId;
                body = "Your order #" + orderId + " has been confirmed and is being processed.";
            }
            case "ORDER_SHIPPED" -> {
                subject = "Order Shipped - #" + orderId;
                body = "Your order #" + orderId + " has been shipped.";
            }
            case "ORDER_DELIVERED" -> {
                subject = "Order Delivered - #" + orderId;
                body = "Your order #" + orderId + " has been delivered.";
            }
            case "ORDER_CANCELLED" -> {
                subject = "Order Cancelled - #" + orderId;
                body = "Your order #" + orderId + " has been cancelled.";
            }
            default -> {
                log.debug("Ignoring order event type: {}", type);
                return;
            }
        }

        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId(userId)
                .recipientEmail(email)
                .channel(NotificationChannel.EMAIL)
                .subject(subject)
                .body(body)
                .eventType(type)
                .build();

        notificationService.sendNotification(request);
    }
}
