package com.dembasiby.cartservice.dto.response.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class CartEvent {
    private String eventId;
    private CartEventType type;
    private String userId;
    private String productId;
    private Integer quantity;
    private LocalDateTime timestamp;
}
