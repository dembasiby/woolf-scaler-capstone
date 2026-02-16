package com.dembasiby.userservice.dto.response.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetEvent {
    private String eventId;
    private String token;
    private String email;
    private Long userId;
    private LocalDateTime timestamp;
}
