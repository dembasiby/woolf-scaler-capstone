package com.dembasiby.orderservice.service;

import com.dembasiby.orderservice.dto.request.CreateOrderItemRequest;
import com.dembasiby.orderservice.dto.request.CreateOrderRequest;
import com.dembasiby.orderservice.dto.response.OrderDto;
import com.dembasiby.orderservice.dto.response.event.OrderEvent;
import com.dembasiby.orderservice.exception.NotFoundException;
import com.dembasiby.orderservice.model.Order;
import com.dembasiby.orderservice.model.OrderItem;
import com.dembasiby.orderservice.model.OrderStatus;
import com.dembasiby.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderEventProducer orderEventProducer;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;

    @BeforeEach
    void setUp() {
        OrderItem item = OrderItem.builder()
                .id(1L)
                .productId("prod-1")
                .productName("Test Product")
                .quantity(2)
                .price(new BigDecimal("25.00"))
                .build();

        testOrder = Order.builder()
                .id(1L)
                .userId("user-1")
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("50.00"))
                .shippingAddress("123 Main St")
                .items(new ArrayList<>(List.of(item)))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        item.setOrder(testOrder);
    }

    @Test
    void createOrder_success() {
        CreateOrderItemRequest itemRequest = CreateOrderItemRequest.builder()
                .productId("prod-1")
                .productName("Test Product")
                .quantity(2)
                .price(new BigDecimal("25.00"))
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .userId("user-1")
                .shippingAddress("123 Main St")
                .items(List.of(itemRequest))
                .build();

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });

        OrderDto result = orderService.createOrder(request);

        assertNotNull(result);
        assertEquals("user-1", result.getUserId());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        verify(orderEventProducer).publish(any(OrderEvent.class));
    }

    @Test
    void getOrderById_success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        OrderDto result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user-1", result.getUserId());
    }

    @Test
    void getOrderById_notFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.getOrderById(99L));
    }

    @Test
    void getOrdersByUserId_success() {
        when(orderRepository.findByUserIdOrderByCreatedAtDesc("user-1")).thenReturn(List.of(testOrder));

        List<OrderDto> result = orderService.getOrdersByUserId("user-1");

        assertEquals(1, result.size());
        assertEquals("user-1", result.get(0).getUserId());
    }

    @Test
    void updateOrderStatus_success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderDto result = orderService.updateOrderStatus(1L, OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, testOrder.getStatus());
        verify(orderEventProducer).publish(any(OrderEvent.class));
    }

    @Test
    void updateOrderStatus_notFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.updateOrderStatus(99L, OrderStatus.CONFIRMED));
    }

    @Test
    void cancelOrder_success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        OrderDto result = orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELLED, testOrder.getStatus());
        verify(orderEventProducer).publish(any(OrderEvent.class));
    }

    @Test
    void cancelOrder_notFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.cancelOrder(99L));
    }
}
