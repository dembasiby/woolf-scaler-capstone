package com.dembasiby.cartservice.dto.response.event;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ProductUpdateEvent {
    private String productId;
    private String title;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private String categoryName;
    private boolean isDeleted;
}
