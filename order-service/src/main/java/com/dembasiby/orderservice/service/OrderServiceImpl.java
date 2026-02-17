package com.dembasiby.orderservice.service;

import com.dembasiby.orderservice.dto.request.CreateOrderRequest;
import com.dembasiby.orderservice.dto.response.OrderDto;
import com.dembasiby.orderservice.dto.response.event.OrderEvent;
import com.dembasiby.orderservice.dto.response.event.OrderEventType;
import com.dembasiby.orderservice.exception.NotFoundException;
import com.dembasiby.orderservice.mapper.OrderMapper;
import com.dembasiby.orderservice.model.Order;
import com.dembasiby.orderservice.model.OrderStatus;
import com.dembasiby.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    @Override
    public OrderDto createOrder(CreateOrderRequest request, String userId) {
        Order order = OrderMapper.toOrder(request, userId);
        Order savedOrder = orderRepository.save(order);

        publishEvent(savedOrder, OrderEventType.ORDER_CREATED);

        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public List<OrderDto> getOrdersByUserId(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(OrderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        OrderEventType eventType = mapStatusToEventType(status);
        publishEvent(updatedOrder, eventType);

        return OrderMapper.toOrderDto(updatedOrder);
    }

    @Override
    public OrderDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        order.setStatus(OrderStatus.CANCELLED);
        Order cancelledOrder = orderRepository.save(order);

        publishEvent(cancelledOrder, OrderEventType.ORDER_CANCELLED);

        return OrderMapper.toOrderDto(cancelledOrder);
    }

    private void publishEvent(Order order, OrderEventType type) {
        OrderEvent event = OrderEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .type(type)
                .orderId(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .timestamp(LocalDateTime.now())
                .build();

        orderEventProducer.publish(event);
    }

    private OrderEventType mapStatusToEventType(OrderStatus status) {
        return switch (status) {
            case CONFIRMED -> OrderEventType.ORDER_CONFIRMED;
            case SHIPPED -> OrderEventType.ORDER_SHIPPED;
            case DELIVERED -> OrderEventType.ORDER_DELIVERED;
            case CANCELLED -> OrderEventType.ORDER_CANCELLED;
            default -> OrderEventType.ORDER_CREATED;
        };
    }
}
