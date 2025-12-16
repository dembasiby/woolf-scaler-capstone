package com.dembasiby.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductDto {
    @NotBlank
    private String title;
    private String description;
    private String imageUrl;

    @NotNull
    @Positive
    private Double price;

    @NotEmpty
    private Long categoryId;
}
