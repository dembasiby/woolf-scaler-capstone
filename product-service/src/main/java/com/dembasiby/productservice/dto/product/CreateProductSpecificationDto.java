package com.dembasiby.productservice.dto.product;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CreateProductSpecificationDto {
    @NotEmpty
    private String specKey;
    @NotEmpty
    private String specValue;
}
