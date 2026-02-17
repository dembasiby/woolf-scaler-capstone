package com.dembasiby.paymentservice.controller;

import com.dembasiby.paymentservice.dto.request.CreatePaymentRequest;
import com.dembasiby.paymentservice.dto.response.PaymentDto;
import com.dembasiby.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(Authentication authentication,
                                                     @Valid @RequestBody CreatePaymentRequest request) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(request, userId));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentDto> getPaymentById(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDto> getPaymentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PaymentDto>> getMyPayments(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(paymentService.getPaymentsByUserId(userId));
    }
}
