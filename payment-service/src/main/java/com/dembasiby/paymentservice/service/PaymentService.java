package com.dembasiby.paymentservice.service;

import com.dembasiby.paymentservice.dto.request.CreatePaymentRequest;
import com.dembasiby.paymentservice.dto.response.PaymentDto;

import java.util.List;

public interface PaymentService {
    PaymentDto createPayment(CreatePaymentRequest request);
    PaymentDto getPaymentById(Long paymentId);
    PaymentDto getPaymentByOrderId(Long orderId);
    List<PaymentDto> getPaymentsByUserId(String userId);
}
