package com.dembasiby.cartservice.dto.response.product;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class ProductSummaryDto {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;
    private String categoryName;
}
