package com.dembasiby.cartservice.controller;

import com.dembasiby.cartservice.config.JwtAuthenticationFilter;
import com.dembasiby.cartservice.config.JwtService;
import com.dembasiby.cartservice.config.SecurityConfig;
import com.dembasiby.cartservice.dto.request.AddToCartRequest;
import com.dembasiby.cartservice.dto.response.CartDto;
import com.dembasiby.cartservice.dto.response.CartItemDto;
import com.dembasiby.cartservice.service.CartService;
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

@WebMvcTest(CartController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, JwtService.class})
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CartService cartService;

    private UsernamePasswordAuthenticationToken authToken() {
        return new UsernamePasswordAuthenticationToken(1L, null, List.of());
    }

    @Test
    void getCart_returns200() throws Exception {
        CartDto response = CartDto.builder()
                .userId("1")
                .items(List.of(CartItemDto.builder()
                        .productId("prod-1")
                        .productName("Test Product")
                        .price(new BigDecimal("25.00"))
                        .quantity(2)
                        .build()))
                .build();

        when(cartService.getCart("1")).thenReturn(response);

        mockMvc.perform(get("/")
                        .with(authentication(authToken())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.items[0].productId").value("prod-1"));
    }

    @Test
    void getCart_returns403WhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isForbidden());
    }

    @Test
    void addToCart_returns200() throws Exception {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId("prod-1");
        request.setQuantity(2);

        CartDto response = CartDto.builder()
                .userId("1")
                .items(List.of(CartItemDto.builder()
                        .productId("prod-1")
                        .productName("Test Product")
                        .price(new BigDecimal("25.00"))
                        .quantity(2)
                        .build()))
                .build();

        when(cartService.addToCart(eq("1"), any(AddToCartRequest.class))).thenReturn(response);

        mockMvc.perform(post("/items")
                        .with(authentication(authToken()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value("prod-1"));
    }

    @Test
    void updateItemQuantity_returns200() throws Exception {
        CartDto response = CartDto.builder()
                .userId("1")
                .items(List.of(CartItemDto.builder()
                        .productId("prod-1")
                        .productName("Test Product")
                        .price(new BigDecimal("25.00"))
                        .quantity(5)
                        .build()))
                .build();

        when(cartService.updateItemQuantity("1", "prod-1", 5)).thenReturn(response);

        mockMvc.perform(put("/items/prod-1")
                        .with(authentication(authToken()))
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].quantity").value(5));
    }

    @Test
    void removeItem_returns200() throws Exception {
        CartDto response = CartDto.builder()
                .userId("1")
                .items(List.of())
                .build();

        when(cartService.removeItem("1", "prod-1")).thenReturn(response);

        mockMvc.perform(delete("/items/prod-1")
                        .with(authentication(authToken())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }
}
