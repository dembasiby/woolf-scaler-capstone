package com.dembasiby.productservice.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor
public class ProductCategoryDto {
    private String id;
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
}
