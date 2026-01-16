package com.dembasiby.productservice.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ProductSpecificationDto {
    private String specKey;
    private String specValue;
}
