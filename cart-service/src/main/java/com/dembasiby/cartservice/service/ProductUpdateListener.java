package com.dembasiby.cartservice.service;

import com.dembasiby.cartservice.dto.response.event.ProductUpdateEvent;
import com.dembasiby.cartservice.model.Cart;
import com.dembasiby.cartservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductUpdateListener {

    private final CartRepository cartRepository;

    @KafkaListener(topics = "product-updates", groupId = "cart-service-group")
    public void consume(ProductUpdateEvent event) {
        log.info("Product update received: {}", event.getProductId());

        List<Cart> carts = cartRepository.findByItemsProductId(event.getProductId());
        carts.forEach(cart -> {
            cart.getItems().forEach(item -> {
                if (item.getProductId().equals(event.getProductId())) {
                    item.setProductName(event.getTitle());
                    item.setProductDescription(event.getDescription());
                    item.setImageUrl(event.getImageUrl());
                    item.setPrice(event.getPrice());
                    item.setCategoryName(event.getCategoryName());
                }
            });
            cartRepository.save(cart);
        });
        log.info("Updated {} carts", carts.size());
    }

}
