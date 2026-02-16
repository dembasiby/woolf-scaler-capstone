package com.dembasiby.paymentservice.repository;

import com.dembasiby.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
    List<Payment> findByUserIdOrderByCreatedAtDesc(String userId);
}
