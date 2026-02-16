package com.dembasiby.notificationservice.mapper;

import com.dembasiby.notificationservice.dto.request.SendNotificationRequest;
import com.dembasiby.notificationservice.dto.response.NotificationDto;
import com.dembasiby.notificationservice.model.Notification;
import com.dembasiby.notificationservice.model.NotificationChannel;
import com.dembasiby.notificationservice.model.NotificationStatus;

public class NotificationMapper {

    public static Notification toNotification(SendNotificationRequest request) {
        return Notification.builder()
                .userId(request.getUserId())
                .recipientEmail(request.getRecipientEmail())
                .channel(request.getChannel() != null ? request.getChannel() : NotificationChannel.EMAIL)
                .subject(request.getSubject())
                .body(request.getBody())
                .eventType(request.getEventType())
                .status(NotificationStatus.PENDING)
                .build();
    }

    public static NotificationDto toNotificationDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .recipientEmail(notification.getRecipientEmail())
                .channel(notification.getChannel())
                .subject(notification.getSubject())
                .body(notification.getBody())
                .eventType(notification.getEventType())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
}
