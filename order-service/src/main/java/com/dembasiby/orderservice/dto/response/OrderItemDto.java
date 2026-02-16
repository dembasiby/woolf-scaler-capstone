package com.dembasiby.orderservice.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OrderItemDto {
    private Long id;
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
