package com.dembasiby.productservice.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotBlank
    private String id;
    @NotBlank
    private String title;
    private String description;
    private String imageUrl;
    @NotBlank
    private String price;

    @NotNull
    private long categoryId;
    @NotBlank
    private String categoryName;
}
