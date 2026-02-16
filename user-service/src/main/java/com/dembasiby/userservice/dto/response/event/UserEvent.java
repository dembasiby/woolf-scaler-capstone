package com.dembasiby.userservice.dto.response.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEvent {
    private String eventId;
    private UserEventType type;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime timestamp;
}
