package com.dembasiby.cartservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    @NotBlank
    private String productId;

    @Min(1)
    private Integer quantity = 1;

    // These will be populated by calling Product Service (or via async event)
    private String productName;
    private String imageUrl;
    private BigDecimal price;
}
