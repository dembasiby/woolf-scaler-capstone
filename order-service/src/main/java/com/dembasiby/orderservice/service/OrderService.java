package com.dembasiby.orderservice.service;

import com.dembasiby.orderservice.dto.request.CreateOrderRequest;
import com.dembasiby.orderservice.dto.response.OrderDto;
import com.dembasiby.orderservice.model.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(CreateOrderRequest request);
    OrderDto getOrderById(Long orderId);
    List<OrderDto> getOrdersByUserId(String userId);
    OrderDto updateOrderStatus(Long orderId, OrderStatus status);
    OrderDto cancelOrder(Long orderId);
}
