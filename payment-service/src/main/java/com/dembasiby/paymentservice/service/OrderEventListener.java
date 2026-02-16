package com.dembasiby.paymentservice.service;

import com.dembasiby.paymentservice.dto.request.CreatePaymentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventListener {

    private final PaymentService paymentService;

    @KafkaListener(topics = "order-events", groupId = "payment-group")
    public void consume(Map<String, Object> event) {
        String type = (String) event.get("type");

        if ("ORDER_CONFIRMED".equals(type)) {
            log.info("Order confirmed event received for order: {}", event.get("orderId"));

            CreatePaymentRequest request = CreatePaymentRequest.builder()
                    .orderId(((Number) event.get("orderId")).longValue())
                    .userId((String) event.get("userId"))
                    .amount(new BigDecimal(event.get("totalAmount").toString()))
                    .currency("USD")
                    .build();

            paymentService.createPayment(request);
            log.info("Payment initiated for order: {}", event.get("orderId"));
        }
    }
}
