package com.dembasiby.cartservice.controller;

import com.dembasiby.cartservice.dto.request.AddToCartRequest;
import com.dembasiby.cartservice.dto.response.CartDto;
import com.dembasiby.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/")
    public ResponseEntity<CartDto> getCart(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addToCart(
            Authentication authentication,
            @Valid @RequestBody AddToCartRequest request) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartDto> updateItemQuantity(
            Authentication authentication,
            @PathVariable String productId,
            @RequestParam int quantity
    ) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, productId, quantity));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartDto> removeItem(
            Authentication authentication,
            @PathVariable String productId) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(cartService.removeItem(userId, productId));
    }
}
