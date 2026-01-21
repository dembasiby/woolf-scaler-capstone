package com.dembasiby.productservice.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dembasiby.productservice.model.Product}
 */
@AllArgsConstructor
@Getter
public class UpdateProductDto implements Serializable {
    @NotBlank
    private final String title;
    private final String description;
    @NotNull
    @Positive
    private final BigDecimal price;
    private final String imageUrl;
}