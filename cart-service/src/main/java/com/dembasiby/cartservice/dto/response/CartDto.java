package com.dembasiby.cartservice.dto.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String userId;
    private List<CartItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
