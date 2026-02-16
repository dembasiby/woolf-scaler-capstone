package com.dembasiby.notificationservice.service;

import com.dembasiby.notificationservice.dto.request.SendNotificationRequest;
import com.dembasiby.notificationservice.model.NotificationChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventListener {

    private final NotificationService notificationService;

    @KafkaListener(topics = "payment-events", groupId = "notification-group")
    public void consume(Map<String, Object> event) {
        String type = (String) event.get("type");
        log.info("Received payment event: {}", type);

        String userId = (String) event.get("userId");
        Long orderId = ((Number) event.get("orderId")).longValue();
        BigDecimal amount = new BigDecimal(event.get("amount").toString());
        String email = event.get("email") != null ? (String) event.get("email") : userId;

        String subject;
        String body;

        switch (type) {
            case "PAYMENT_COMPLETED" -> {
                subject = "Payment Successful - Order #" + orderId;
                body = "Your payment of $" + amount + " for order #" + orderId + " has been processed successfully.";
            }
            case "PAYMENT_FAILED" -> {
                subject = "Payment Failed - Order #" + orderId;
                body = "Your payment of $" + amount + " for order #" + orderId + " has failed. Please try again.";
            }
            default -> {
                log.debug("Ignoring payment event type: {}", type);
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
