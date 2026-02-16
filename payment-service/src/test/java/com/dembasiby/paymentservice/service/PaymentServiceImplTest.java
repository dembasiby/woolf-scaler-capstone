package com.dembasiby.paymentservice.service;

import com.dembasiby.paymentservice.dto.request.CreatePaymentRequest;
import com.dembasiby.paymentservice.dto.response.PaymentDto;
import com.dembasiby.paymentservice.dto.response.event.PaymentEvent;
import com.dembasiby.paymentservice.exception.NotFoundException;
import com.dembasiby.paymentservice.exception.PaymentException;
import com.dembasiby.paymentservice.model.Payment;
import com.dembasiby.paymentservice.model.PaymentStatus;
import com.dembasiby.paymentservice.repository.PaymentRepository;
import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private StripeService stripeService;
    @Mock
    private PaymentEventProducer paymentEventProducer;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment testPayment;

    @BeforeEach
    void setUp() {
        testPayment = Payment.builder()
                .id(1L)
                .orderId(100L)
                .userId("user-1")
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .status(PaymentStatus.COMPLETED)
                .stripePaymentIntentId("pi_test123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createPayment_success() {
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .orderId(100L)
                .userId("user-1")
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .build();

        PaymentIntent mockIntent = mock(PaymentIntent.class);
        when(mockIntent.getId()).thenReturn("pi_test123");

        when(stripeService.createPaymentIntent(new BigDecimal("99.99"), "USD")).thenReturn(mockIntent);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        PaymentDto result = paymentService.createPayment(request);

        assertNotNull(result);
        assertEquals(PaymentStatus.COMPLETED, result.getStatus());
        assertEquals("pi_test123", result.getStripePaymentIntentId());
        verify(paymentEventProducer).publish(any(PaymentEvent.class));
    }

    @Test
    void createPayment_stripeFailure() {
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .orderId(100L)
                .userId("user-1")
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .build();

        when(stripeService.createPaymentIntent(any(), anyString()))
                .thenThrow(new PaymentException("Payment processing failed"));

        assertThrows(PaymentException.class, () -> paymentService.createPayment(request));
    }

    @Test
    void getPaymentById_success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(testPayment));

        PaymentDto result = paymentService.getPaymentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user-1", result.getUserId());
    }

    @Test
    void getPaymentById_notFound() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.getPaymentById(99L));
    }

    @Test
    void getPaymentByOrderId_success() {
        when(paymentRepository.findByOrderId(100L)).thenReturn(Optional.of(testPayment));

        PaymentDto result = paymentService.getPaymentByOrderId(100L);

        assertNotNull(result);
        assertEquals(100L, result.getOrderId());
    }

    @Test
    void getPaymentByOrderId_notFound() {
        when(paymentRepository.findByOrderId(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.getPaymentByOrderId(999L));
    }

    @Test
    void getPaymentsByUserId_success() {
        when(paymentRepository.findByUserIdOrderByCreatedAtDesc("user-1")).thenReturn(List.of(testPayment));

        List<PaymentDto> result = paymentService.getPaymentsByUserId("user-1");

        assertEquals(1, result.size());
        assertEquals("user-1", result.get(0).getUserId());
    }
}
