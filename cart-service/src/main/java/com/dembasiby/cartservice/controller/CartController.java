package com.dembasiby.cartservice.controller;

import com.dembasiby.cartservice.dto.request.AddToCartRequest;
import com.dembasiby.cartservice.dto.response.CartDto;
import com.dembasiby.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
//@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartDto> addToCart(
            @PathVariable String userId,
            @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(userId, request));
    }

    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartDto> updateItemQuantity(
            @PathVariable String userId,
            @PathVariable String productId,
            @RequestParam int quantity
    ) {
        return ResponseEntity.ok(
                cartService .updateItemQuantity( userId, productId, quantity));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartDto> removeItem(
            @PathVariable String userId, @PathVariable String productId) {
        return ResponseEntity.ok(cartService.removeItem(userId, productId));
    }
}
