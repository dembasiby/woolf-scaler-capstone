package com.dembasiby.cartservice.service;

import com.dembasiby.cartservice.dto.request.AddToCartRequest;
import com.dembasiby.cartservice.dto.response.CartDto;
import com.dembasiby.cartservice.dto.response.event.CartEvent;
import com.dembasiby.cartservice.dto.response.product.ProductSummaryDto;
import com.dembasiby.cartservice.exception.NotFoundException;
import com.dembasiby.cartservice.model.Cart;
import com.dembasiby.cartservice.repository.CartRepository;
import com.dembasiby.cartservice.service.client.ProductServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductServiceClient productServiceClient;
    @Mock
    private CartEventProducer cartEventProducer;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart testCart;
    private ProductSummaryDto testProduct;

    @BeforeEach
    void setUp() {
        testCart = new Cart();
        testCart.setUserId("user-1");
        testCart.setItems(new ArrayList<>());
        testCart.setCreatedAt(LocalDateTime.now());
        testCart.setUpdatedAt(LocalDateTime.now());

        testProduct = new ProductSummaryDto();
        testProduct.setId("prod-1");
        testProduct.setTitle("Test Product");
        testProduct.setDescription("A test product");
        testProduct.setImageUrl("http://example.com/img.jpg");
        testProduct.setPrice(new BigDecimal("25.00"));
        testProduct.setCategoryName("Electronics");
    }

    @Test
    void getCart_existingCart() {
        when(cartRepository.findByUserId("user-1")).thenReturn(Optional.of(testCart));

        CartDto result = cartService.getCart("user-1");

        assertNotNull(result);
        assertEquals("user-1", result.getUserId());
        assertEquals(0, result.getItems().size());
    }

    @Test
    void getCart_newCart() {
        when(cartRepository.findByUserId("user-2")).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartDto result = cartService.getCart("user-2");

        assertNotNull(result);
        assertEquals("user-2", result.getUserId());
        verify(cartRepository).save(any(Cart.class));
        verify(cartEventProducer).publish(any(CartEvent.class));
    }

    @Test
    void addToCart_newItem() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId("prod-1");
        request.setQuantity(2);

        when(cartRepository.findByUserId("user-1")).thenReturn(Optional.of(testCart));
        when(productServiceClient.getProduct("prod-1")).thenReturn(testProduct);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartDto result = cartService.addToCart("user-1", request);

        assertNotNull(result);
        assertEquals(1, testCart.getItems().size());
        assertEquals("prod-1", testCart.getItems().get(0).getProductId());
        assertEquals(2, testCart.getItems().get(0).getQuantity());
        verify(cartEventProducer).publish(any(CartEvent.class));
    }

    @Test
    void addToCart_existingItem() {
        Cart.CartItem existingItem = new Cart.CartItem(
                "prod-1", "Test Product", "Desc", "url",
                new BigDecimal("25.00"), 1, "Electronics");
        testCart.getItems().add(existingItem);

        AddToCartRequest request = new AddToCartRequest();
        request.setProductId("prod-1");
        request.setQuantity(3);

        when(cartRepository.findByUserId("user-1")).thenReturn(Optional.of(testCart));
        when(productServiceClient.getProduct("prod-1")).thenReturn(testProduct);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartDto result = cartService.addToCart("user-1", request);

        assertEquals(1, testCart.getItems().size());
        assertEquals(4, testCart.getItems().get(0).getQuantity());
    }

    @Test
    void updateItemQuantity_success() {
        Cart.CartItem item = new Cart.CartItem(
                "prod-1", "Test Product", "Desc", "url",
                new BigDecimal("25.00"), 2, "Electronics");
        testCart.getItems().add(item);

        when(cartRepository.findByUserId("user-1")).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartDto result = cartService.updateItemQuantity("user-1", "prod-1", 5);

        assertNotNull(result);
        assertEquals(5, testCart.getItems().get(0).getQuantity());
        verify(cartEventProducer).publish(any(CartEvent.class));
    }

    @Test
    void updateItemQuantity_removeWhenZero() {
        Cart.CartItem item = new Cart.CartItem(
                "prod-1", "Test Product", "Desc", "url",
                new BigDecimal("25.00"), 2, "Electronics");
        testCart.getItems().add(item);

        when(cartRepository.findByUserId("user-1")).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartDto result = cartService.updateItemQuantity("user-1", "prod-1", 0);

        assertNotNull(result);
        assertEquals(0, testCart.getItems().size());
        verify(cartEventProducer).publish(any(CartEvent.class));
    }

    @Test
    void updateItemQuantity_cartNotFound() {
        when(cartRepository.findByUserId("user-99")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> cartService.updateItemQuantity("user-99", "prod-1", 5));
    }

    @Test
    void removeItem_success() {
        Cart.CartItem item = new Cart.CartItem(
                "prod-1", "Test Product", "Desc", "url",
                new BigDecimal("25.00"), 2, "Electronics");
        testCart.getItems().add(item);

        when(cartRepository.findByUserId("user-1")).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartDto result = cartService.removeItem("user-1", "prod-1");

        assertNotNull(result);
        assertEquals(0, testCart.getItems().size());
        verify(cartEventProducer).publish(any(CartEvent.class));
    }

    @Test
    void removeItem_cartNotFound() {
        when(cartRepository.findByUserId("user-99")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> cartService.removeItem("user-99", "prod-1"));
    }

    @Test
    void clearCart_success() {
        when(cartRepository.findByUserId("user-1")).thenReturn(Optional.of(testCart));

        cartService.clearCart("user-1");

        verify(cartRepository).deleteById("user-1");
    }
}
