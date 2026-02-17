package com.dembasiby.orderservice.controller;

import com.dembasiby.orderservice.config.JwtAuthenticationFilter;
import com.dembasiby.orderservice.config.JwtService;
import com.dembasiby.orderservice.config.SecurityConfig;
import com.dembasiby.orderservice.dto.request.CreateOrderItemRequest;
import com.dembasiby.orderservice.dto.request.CreateOrderRequest;
import com.dembasiby.orderservice.dto.request.UpdateOrderStatusRequest;
import com.dembasiby.orderservice.dto.response.OrderDto;
import com.dembasiby.orderservice.dto.response.OrderItemDto;
import com.dembasiby.orderservice.exception.NotFoundException;
import com.dembasiby.orderservice.model.OrderStatus;
import com.dembasiby.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtService.class})
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private OrderService orderService;

    private UsernamePasswordAuthenticationToken authToken() {
        return new UsernamePasswordAuthenticationToken(1L, null, List.of());
    }

    @Test
    void createOrder_returns201() throws Exception {
        CreateOrderItemRequest itemRequest = CreateOrderItemRequest.builder()
                .productId("prod-1")
                .productName("Test Product")
                .quantity(2)
                .price(new BigDecimal("25.00"))
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .shippingAddress("123 Main St")
                .items(List.of(itemRequest))
                .build();

        OrderDto response = OrderDto.builder()
                .id(1L)
                .userId("1")
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("50.00"))
                .items(List.of(OrderItemDto.builder()
                        .productId("prod-1")
                        .productName("Test Product")
                        .quantity(2)
                        .price(new BigDecimal("25.00"))
                        .build()))
                .build();

        when(orderService.createOrder(any(CreateOrderRequest.class), eq("1"))).thenReturn(response);

        mockMvc.perform(post("/")
                        .with(authentication(authToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void createOrder_returns403WhenUnauthenticated() throws Exception {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .shippingAddress("123 Main St")
                .items(List.of(CreateOrderItemRequest.builder()
                        .productId("prod-1").productName("P").quantity(1).price(new BigDecimal("10")).build()))
                .build();

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createOrder_returns400ForInvalidInput() throws Exception {
        CreateOrderRequest request = CreateOrderRequest.builder()
                .items(null)
                .build();

        mockMvc.perform(post("/")
                        .with(authentication(authToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrderById_returns200() throws Exception {
        OrderDto response = OrderDto.builder()
                .id(1L)
                .userId("1")
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("50.00"))
                .items(List.of())
                .build();

        when(orderService.getOrderById(1L)).thenReturn(response);

        mockMvc.perform(get("/1")
                        .with(authentication(authToken())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value("1"));
    }

    @Test
    void getOrderById_returns404() throws Exception {
        when(orderService.getOrderById(99L)).thenThrow(new NotFoundException("Order not found with id: 99"));

        mockMvc.perform(get("/99")
                        .with(authentication(authToken())))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMyOrders_returns200() throws Exception {
        OrderDto response = OrderDto.builder()
                .id(1L)
                .userId("1")
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("50.00"))
                .items(List.of())
                .build();

        when(orderService.getOrdersByUserId("1")).thenReturn(List.of(response));

        mockMvc.perform(get("/my")
                        .with(authentication(authToken())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("1"));
    }

    @Test
    void updateOrderStatus_returns200() throws Exception {
        UpdateOrderStatusRequest request = UpdateOrderStatusRequest.builder()
                .status(OrderStatus.CONFIRMED)
                .build();

        OrderDto response = OrderDto.builder()
                .id(1L)
                .userId("1")
                .status(OrderStatus.CONFIRMED)
                .totalAmount(new BigDecimal("50.00"))
                .items(List.of())
                .build();

        when(orderService.updateOrderStatus(eq(1L), eq(OrderStatus.CONFIRMED))).thenReturn(response);

        mockMvc.perform(put("/1/status")
                        .with(authentication(authToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void cancelOrder_returns200() throws Exception {
        OrderDto response = OrderDto.builder()
                .id(1L)
                .userId("1")
                .status(OrderStatus.CANCELLED)
                .totalAmount(new BigDecimal("50.00"))
                .items(List.of())
                .build();

        when(orderService.cancelOrder(1L)).thenReturn(response);

        mockMvc.perform(put("/1/cancel")
                        .with(authentication(authToken())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
