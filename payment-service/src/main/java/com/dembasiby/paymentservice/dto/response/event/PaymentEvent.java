package com.dembasiby.paymentservice.dto.response.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PaymentEvent {
    private String eventId;
    private PaymentEventType type;
    private Long paymentId;
    private Long orderId;
    private String userId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
}
