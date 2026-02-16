package com.dembasiby.orderservice.dto.response.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrderEvent {
    private String eventId;
    private OrderEventType type;
    private Long orderId;
    private String userId;
    private BigDecimal totalAmount;
    private LocalDateTime timestamp;
}
