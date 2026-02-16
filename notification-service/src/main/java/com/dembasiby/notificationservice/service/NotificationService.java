package com.dembasiby.notificationservice.service;

import com.dembasiby.notificationservice.dto.request.SendNotificationRequest;
import com.dembasiby.notificationservice.dto.response.NotificationDto;

import java.util.List;

public interface NotificationService {
    NotificationDto sendNotification(SendNotificationRequest request);
    NotificationDto getNotificationById(Long notificationId);
    List<NotificationDto> getNotificationsByUserId(String userId);
}
