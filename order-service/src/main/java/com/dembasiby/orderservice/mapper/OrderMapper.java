package com.dembasiby.orderservice.mapper;

import com.dembasiby.orderservice.dto.request.CreateOrderItemRequest;
import com.dembasiby.orderservice.dto.request.CreateOrderRequest;
import com.dembasiby.orderservice.dto.response.OrderDto;
import com.dembasiby.orderservice.dto.response.OrderItemDto;
import com.dembasiby.orderservice.model.Order;
import com.dembasiby.orderservice.model.OrderItem;
import com.dembasiby.orderservice.model.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public class OrderMapper {

    public static Order toOrder(CreateOrderRequest request, String userId) {
        List<OrderItem> items = request.getItems().stream()
                .map(OrderMapper::toOrderItem)
                .toList();

        BigDecimal totalAmount = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userId(userId)
                .shippingAddress(request.getShippingAddress())
                .status(OrderStatus.PENDING)
                .totalAmount(totalAmount)
                .build();

        items.forEach(item -> item.setOrder(order));
        order.setItems(items);

        return order;
    }

    public static OrderDto toOrderDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .items(order.getItems().stream()
                        .map(OrderMapper::toOrderItemDto)
                        .toList())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public static OrderItem toOrderItem(CreateOrderItemRequest request) {
        return OrderItem.builder()
                .productId(request.getProductId())
                .productName(request.getProductName())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .build();
    }

    public static OrderItemDto toOrderItemDto(OrderItem item) {
        return OrderItemDto.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }
}
