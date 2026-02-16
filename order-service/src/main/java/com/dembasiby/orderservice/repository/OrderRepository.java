package com.dembasiby.orderservice.repository;

import com.dembasiby.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
}
