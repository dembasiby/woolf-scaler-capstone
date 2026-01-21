package com.dembasiby.cartservice.service;

import com.dembasiby.cartservice.dto.request.AddToCartRequest;
import com.dembasiby.cartservice.dto.response.CartDto;
import com.dembasiby.cartservice.dto.response.product.ProductSummaryDto;
import com.dembasiby.cartservice.exception.NotFoundException;
import com.dembasiby.cartservice.mapper.CartMapper;
import com.dembasiby.cartservice.model.Cart;
import com.dembasiby.cartservice.repository.CartRepository;
import com.dembasiby.cartservice.service.client.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductServiceClient productServiceClient;

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
        System.out.println("=================================================");
        System.out.println("PRODUCT FROM PRODUCT-SERVICE = " + product);
        System.out.println("DESC = " + product.getDescription());
        System.out.println("CAT  = " + product.getCategoryName());
        System.out.println("=================================================");


        Boolean exists = cart.getItems().stream()
                .anyMatch(
                        item -> item.getProductId().equals(request.getProductId()));

        if (exists) {
            cart.getItems().forEach(
                    item -> {
                        if (item.getProductId().equals(request.getProductId())) {
                            item.setQuantity(item.getQuantity() + request.getQuantity());
                        }
                    }
            );
        } else {
            cart.getItems().add(CartMapper.toCartItem(product, request.getQuantity()));
        }

        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
        return CartMapper.toCartDto(cart);
    }

    @Override
    @CachePut(value = "carts", key = "#userId")
    public CartDto updateItemQuantity(String userId, String productId, Integer quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        if (quantity <= 0) {
            cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        } else {
            AtomicBoolean updated = new AtomicBoolean(false);

            cart.getItems().forEach(item -> {
                if (item.getProductId().equals(productId)) {
                    item.setQuantity(quantity);
                    updated.set(true);
                }
            });
            if (!updated.get()) {
                throw new NotFoundException("Item not found in cart");
            }
        }
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
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
        return cartRepository.save(cart);
    }
}
