package com.dembasiby.paymentservice.mapper;

import com.dembasiby.paymentservice.dto.request.CreatePaymentRequest;
import com.dembasiby.paymentservice.dto.response.PaymentDto;
import com.dembasiby.paymentservice.model.Payment;
import com.dembasiby.paymentservice.model.PaymentStatus;

public class PaymentMapper {

    public static Payment toPayment(CreatePaymentRequest request) {
        return Payment.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .currency(request.getCurrency() != null ? request.getCurrency() : "USD")
                .status(PaymentStatus.PENDING)
                .build();
    }

    public static PaymentDto toPaymentDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .stripePaymentIntentId(payment.getStripePaymentIntentId())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }
}
