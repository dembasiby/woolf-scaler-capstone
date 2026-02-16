package com.dembasiby.paymentservice.dto.response;

import com.dembasiby.paymentservice.model.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PaymentDto {
    private Long id;
    private Long orderId;
    private String userId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private String stripePaymentIntentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
