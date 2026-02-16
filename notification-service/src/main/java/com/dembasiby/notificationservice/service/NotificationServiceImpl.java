package com.dembasiby.notificationservice.service;

import com.dembasiby.notificationservice.dto.request.SendNotificationRequest;
import com.dembasiby.notificationservice.dto.response.NotificationDto;
import com.dembasiby.notificationservice.dto.response.event.NotificationEvent;
import com.dembasiby.notificationservice.dto.response.event.NotificationEventType;
import com.dembasiby.notificationservice.exception.NotFoundException;
import com.dembasiby.notificationservice.exception.NotificationException;
import com.dembasiby.notificationservice.mapper.NotificationMapper;
import com.dembasiby.notificationservice.model.Notification;
import com.dembasiby.notificationservice.model.NotificationChannel;
import com.dembasiby.notificationservice.model.NotificationStatus;
import com.dembasiby.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final NotificationEventProducer notificationEventProducer;

    @Override
    public NotificationDto sendNotification(SendNotificationRequest request) {
        Notification notification = NotificationMapper.toNotification(request);
        notification = notificationRepository.save(notification);

        try {
            if (notification.getChannel() == NotificationChannel.EMAIL) {
                emailService.sendEmail(
                        notification.getRecipientEmail(),
                        notification.getSubject(),
                        notification.getBody()
                );
            } else {
                log.warn("SMS channel not yet implemented. Logging notification for user: {}", notification.getUserId());
            }

            notification.setStatus(NotificationStatus.SENT);
            notification = notificationRepository.save(notification);
            publishEvent(notification, NotificationEventType.NOTIFICATION_SENT);
        } catch (NotificationException e) {
            notification.setStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);
            publishEvent(notification, NotificationEventType.NOTIFICATION_FAILED);
            throw e;
        }

        return NotificationMapper.toNotificationDto(notification);
    }

    @Override
    public NotificationDto getNotificationById(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotFoundException("Notification not found with id: " + notificationId));
        return NotificationMapper.toNotificationDto(notification);
    }

    @Override
    public List<NotificationDto> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationMapper::toNotificationDto)
                .toList();
    }

    private void publishEvent(Notification notification, NotificationEventType type) {
        NotificationEvent event = NotificationEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .type(type)
                .notificationId(notification.getId())
                .userId(notification.getUserId())
                .channel(notification.getChannel())
                .timestamp(LocalDateTime.now())
                .build();

        notificationEventProducer.publish(event);
    }
}
