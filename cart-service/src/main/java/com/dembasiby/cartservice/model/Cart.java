// entity/Cart.java
package com.dembasiby.cartservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    private String userId; // userId — each user has one cart

    private List<CartItem> items = new ArrayList<>();
    private CartStatus cartStatus = CartStatus.DEFAULT;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItem {
        private String productId;
        private String productName;
        private String imageUrl;
        private Double price;
        private Integer quantity;
    }
}