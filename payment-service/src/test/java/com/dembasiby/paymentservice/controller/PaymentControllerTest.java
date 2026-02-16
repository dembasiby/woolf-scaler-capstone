package com.dembasiby.paymentservice.controller;

import com.dembasiby.paymentservice.dto.request.CreatePaymentRequest;
import com.dembasiby.paymentservice.dto.response.PaymentDto;
import com.dembasiby.paymentservice.exception.NotFoundException;
import com.dembasiby.paymentservice.model.PaymentStatus;
import com.dembasiby.paymentservice.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void createPayment_returns201() throws Exception {
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .orderId(100L)
                .userId("user-1")
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .build();

        PaymentDto response = PaymentDto.builder()
                .id(1L)
                .orderId(100L)
                .userId("user-1")
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .status(PaymentStatus.COMPLETED)
                .stripePaymentIntentId("pi_test123")
                .build();

        when(paymentService.createPayment(any(CreatePaymentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(100))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void createPayment_returns400ForInvalidInput() throws Exception {
        CreatePaymentRequest request = CreatePaymentRequest.builder()
                .orderId(null)
                .userId("")
                .amount(null)
                .build();

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPaymentById_returns200() throws Exception {
        PaymentDto response = PaymentDto.builder()
                .id(1L)
                .orderId(100L)
                .userId("user-1")
                .amount(new BigDecimal("99.99"))
                .status(PaymentStatus.COMPLETED)
                .build();

        when(paymentService.getPaymentById(1L)).thenReturn(response);

        mockMvc.perform(get("/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value("user-1"));
    }

    @Test
    void getPaymentByOrderId_returns200() throws Exception {
        PaymentDto response = PaymentDto.builder()
                .id(1L)
                .orderId(100L)
                .userId("user-1")
                .amount(new BigDecimal("99.99"))
                .status(PaymentStatus.COMPLETED)
                .build();

        when(paymentService.getPaymentByOrderId(100L)).thenReturn(response);

        mockMvc.perform(get("/payments/order/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(100));
    }

    @Test
    void getPaymentsByUserId_returns200() throws Exception {
        PaymentDto response = PaymentDto.builder()
                .id(1L)
                .orderId(100L)
                .userId("user-1")
                .amount(new BigDecimal("99.99"))
                .status(PaymentStatus.COMPLETED)
                .build();

        when(paymentService.getPaymentsByUserId("user-1")).thenReturn(List.of(response));

        mockMvc.perform(get("/payments/user/user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user-1"));
    }
}
