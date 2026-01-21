package com.dembasiby.cartservice.dto.response;


import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class CartItemDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String productId;
    private String productName;
    private String productDescription;
    private String imageUrl;
    private BigDecimal price;
    private Integer quantity;
    private String categoryName;
}
