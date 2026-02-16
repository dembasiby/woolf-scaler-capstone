package com.dembasiby.notificationservice.dto.response.event;

import com.dembasiby.notificationservice.model.NotificationChannel;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {

    private String eventId;
    private NotificationEventType type;
    private Long notificationId;
    private String userId;
    private NotificationChannel channel;
    private LocalDateTime timestamp;
}
