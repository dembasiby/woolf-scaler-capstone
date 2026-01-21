package com.dembasiby.cartservice.dto.res;


import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class CartItemDto {
    private String productId;
    private String productName;
    private String imageUrl;
    private Double price;
    private Integer quantity;
}
