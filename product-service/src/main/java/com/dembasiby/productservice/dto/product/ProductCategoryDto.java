package com.dembasiby.productservice.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ProductCategoryDto {
    private String id;
    private String title;
    private String description;
    private String price;
    private String imageUrl;
}
