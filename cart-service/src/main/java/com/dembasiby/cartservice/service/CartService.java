package com.dembasiby.cartservice.service;

import com.dembasiby.cartservice.dto.request.AddToCartRequest;
import com.dembasiby.cartservice.dto.response.CartDto;
import org.jspecify.annotations.Nullable;

public interface CartService {
    CartDto getCart(String userId);
    CartDto addToCart(String userId, AddToCartRequest request);
    CartDto updateItemQuantity(String userId, String productId, Integer quantity);
    CartDto removeItem(String userId, String productId);
    void clearCart(String userId);
}
