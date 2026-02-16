package com.dembasiby.cartservice.service;

import com.dembasiby.cartservice.dto.request.AddToCartRequest;
import com.dembasiby.cartservice.dto.response.CartDto;
import com.dembasiby.cartservice.dto.response.event.CartEvent;
import com.dembasiby.cartservice.dto.response.event.CartEventType;
import com.dembasiby.cartservice.dto.response.product.ProductSummaryDto;
import com.dembasiby.cartservice.exception.NotFoundException;
import com.dembasiby.cartservice.mapper.CartMapper;
import com.dembasiby.cartservice.model.Cart;
import com.dembasiby.cartservice.repository.CartRepository;
import com.dembasiby.cartservice.service.client.ProductServiceClient;
import jdk.jfr.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductServiceClient productServiceClient;
    private final CartEventProducer cartEventProducer;

    @Override
    @Cacheable(value = "carts", key = "#userId")
    public CartDto getCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(
                () -> createEmtptyCart(userId)
        );

        return CartMapper.toCartDto(cart);
    }


    @Override
    @CacheEvict(value = "carts", key = "#userId")
    public CartDto addToCart(String userId, AddToCartRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createEmtptyCart(userId));


        ProductSummaryDto product = productServiceClient.getProduct(request.getProductId());

        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst()
                .ifPresentOrElse(item ->
                    item.setQuantity(item.getQuantity() + request.getQuantity()),
                            () -> {
                                Cart.CartItem newItem = CartMapper.toCartItem(product, request.getQuantity());
                                cart.getItems().add(newItem);
                            });


        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        publishEvent( userId,
                request.getProductId(),
                request.getQuantity(),
                CartEventType.ITEM_ADDED);

        return CartMapper.toCartDto(cart);
    }

    @Override
    @CachePut(value = "carts", key = "#userId")
    public CartDto updateItemQuantity(String userId, String productId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        CartEventType type;

        if (quantity <= 0) {
            boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(productId));

            if (!removed) {
                throw new NotFoundException("Item not found in cart");
            }

            type = CartEventType.ITEM_REMOVED;

        } else {

            Cart.CartItem item = cart.getItems().stream()
                            .filter(i -> i.getProductId().equals(productId))
                                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Item not found in cart"));
            item.setQuantity(quantity);
            type = CartEventType.ITEM_UPDATED;
        }
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        publishEvent(userId, productId, quantity, type);
        return CartMapper.toCartDto(cart);
    }

    @Override
    @CacheEvict(value = "carts", key = "#userId")
    public CartDto removeItem(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);

        publishEvent(userId, productId, null, CartEventType.ITEM_REMOVED);
        return CartMapper.toCartDto(cart);
    }

    @Override
    @CacheEvict(value = "carts", key = "#userId")
    public void clearCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        cartRepository.deleteById(userId);
    }

    private Cart createEmtptyCart(String userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        Cart savedCart = cartRepository.save(cart);
        publishEvent(userId, null, null, CartEventType.CART_CREATED);
        return savedCart;
    }

    private void publishEvent(String userId, String productId,
                              Integer quantity, CartEventType type) {
        CartEvent event = CartEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .type(type)
                .userId(userId)
                .productId(productId)
                .quantity(quantity)
                .timestamp(LocalDateTime.now())
                .build();

        cartEventProducer.publish(event);
    }

}
