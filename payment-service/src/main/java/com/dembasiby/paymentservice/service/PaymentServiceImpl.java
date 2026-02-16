package com.dembasiby.paymentservice.service;

import com.dembasiby.paymentservice.dto.request.CreatePaymentRequest;
import com.dembasiby.paymentservice.dto.response.PaymentDto;
import com.dembasiby.paymentservice.dto.response.event.PaymentEvent;
import com.dembasiby.paymentservice.dto.response.event.PaymentEventType;
import com.dembasiby.paymentservice.exception.NotFoundException;
import com.dembasiby.paymentservice.mapper.PaymentMapper;
import com.dembasiby.paymentservice.model.Payment;
import com.dembasiby.paymentservice.model.PaymentStatus;
import com.dembasiby.paymentservice.repository.PaymentRepository;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;
    private final PaymentEventProducer paymentEventProducer;

    @Override
    public PaymentDto createPayment(CreatePaymentRequest request) {
        Payment payment = PaymentMapper.toPayment(request);

        PaymentIntent intent = stripeService.createPaymentIntent(
                request.getAmount(),
                request.getCurrency() != null ? request.getCurrency() : "USD"
        );

        payment.setStripePaymentIntentId(intent.getId());
        payment.setStatus(PaymentStatus.COMPLETED);
        Payment savedPayment = paymentRepository.save(payment);

        publishEvent(savedPayment, PaymentEventType.PAYMENT_COMPLETED);

        return PaymentMapper.toPaymentDto(savedPayment);
    }

    @Override
    public PaymentDto getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + paymentId));
        return PaymentMapper.toPaymentDto(payment);
    }

    @Override
    public PaymentDto getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Payment not found for order: " + orderId));
        return PaymentMapper.toPaymentDto(payment);
    }

    @Override
    public List<PaymentDto> getPaymentsByUserId(String userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(PaymentMapper::toPaymentDto)
                .toList();
    }

    private void publishEvent(Payment payment, PaymentEventType type) {
        PaymentEvent event = PaymentEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .type(type)
                .paymentId(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .timestamp(LocalDateTime.now())
                .build();

        paymentEventProducer.publish(event);
    }
}
