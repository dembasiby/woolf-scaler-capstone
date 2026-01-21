package com.dembasiby.productservice.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchDto {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private String categoryName;
}
