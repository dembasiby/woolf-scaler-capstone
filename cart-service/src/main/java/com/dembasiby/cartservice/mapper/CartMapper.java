package com.dembasiby.cartservice.mapper;

import com.dembasiby.cartservice.dto.request.AddToCartRequest;
import com.dembasiby.cartservice.dto.response.CartItemDto;
import com.dembasiby.cartservice.dto.response.CartDto;
import com.dembasiby.cartservice.dto.response.product.ProductSummaryDto;
import com.dembasiby.cartservice.model.Cart;

public class CartMapper {
    public static CartDto toCartDto(Cart cart) {
        if (cart == null) return null;
        return CartDto.builder()
                .userId(cart.getUserId())
                .items(cart.getItems().stream()
                                .map(CartMapper::toCartItemDto)
                                .toList())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    public static CartItemDto toCartItemDto(Cart.CartItem cartItem) {
        if (cartItem == null) return null;
        return CartItemDto.builder()
                .productId(cartItem.getProductId())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .productName(cartItem.getProductName())
                .productDescription(cartItem.getProductDescription())
                .categoryName(cartItem.getCategoryName())
                .imageUrl(cartItem.getImageUrl())
                .build();
    }

    public static Cart.CartItem toCartItem(AddToCartRequest request) {
       if (request == null) return null;
        Cart.CartItem item = new Cart.CartItem();
        item.setProductId(request.getProductId());
        item.setProductName(request.getProductName());
        item.setImageUrl(request.getImageUrl());
        item.setPrice(request.getPrice());
        item.setQuantity(request.getQuantity());
        return item;
    }

    public static Cart.CartItem toCartItem(ProductSummaryDto product, int quantity) {
        Cart.CartItem newItem = new Cart.CartItem();
        newItem.setProductId(product.getId());
        newItem.setProductName(product.getTitle());
        newItem.setProductDescription(product.getDescription());
        newItem.setImageUrl(product.getImageUrl());
        newItem.setQuantity(quantity);
        newItem.setPrice(product.getPrice());
        newItem.setCategoryName(product.getCategoryName());

        return newItem;
    }
}
