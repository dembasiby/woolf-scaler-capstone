package com.dembasiby.cartservice.repository;

import com.dembasiby.cartservice.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
    List<Cart> findByItemsProductId(String productId);
}
