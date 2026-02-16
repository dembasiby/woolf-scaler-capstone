package com.dembasiby.notificationservice.dto.response;

import com.dembasiby.notificationservice.model.NotificationChannel;
import com.dembasiby.notificationservice.model.NotificationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

    private Long id;
    private String userId;
    private String recipientEmail;
    private NotificationChannel channel;
    private String subject;
    private String body;
    private String eventType;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
