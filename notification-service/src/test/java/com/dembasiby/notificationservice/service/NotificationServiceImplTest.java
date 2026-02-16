package com.dembasiby.notificationservice.service;

import com.dembasiby.notificationservice.dto.request.SendNotificationRequest;
import com.dembasiby.notificationservice.dto.response.NotificationDto;
import com.dembasiby.notificationservice.dto.response.event.NotificationEvent;
import com.dembasiby.notificationservice.exception.NotFoundException;
import com.dembasiby.notificationservice.exception.NotificationException;
import com.dembasiby.notificationservice.model.Notification;
import com.dembasiby.notificationservice.model.NotificationChannel;
import com.dembasiby.notificationservice.model.NotificationStatus;
import com.dembasiby.notificationservice.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private NotificationEventProducer notificationEventProducer;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification testNotification;

    @BeforeEach
    void setUp() {
        testNotification = Notification.builder()
                .id(1L)
                .userId("user-1")
                .recipientEmail("user@example.com")
                .channel(NotificationChannel.EMAIL)
                .subject("Test Subject")
                .body("Test Body")
                .eventType("ORDER_CONFIRMED")
                .status(NotificationStatus.SENT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void sendNotification_emailSuccess() {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId("user-1")
                .recipientEmail("user@example.com")
                .channel(NotificationChannel.EMAIL)
                .subject("Test Subject")
                .body("Test Body")
                .eventType("ORDER_CONFIRMED")
                .build();

        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification n = invocation.getArgument(0);
            n.setId(1L);
            return n;
        });

        NotificationDto result = notificationService.sendNotification(request);

        assertNotNull(result);
        assertEquals(NotificationStatus.SENT, result.getStatus());
        verify(emailService).sendEmail("user@example.com", "Test Subject", "Test Body");
        verify(notificationEventProducer).publish(any(NotificationEvent.class));
    }

    @Test
    void sendNotification_emailFailure() {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId("user-1")
                .recipientEmail("user@example.com")
                .channel(NotificationChannel.EMAIL)
                .subject("Test Subject")
                .body("Test Body")
                .eventType("ORDER_CONFIRMED")
                .build();

        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification n = invocation.getArgument(0);
            n.setId(1L);
            return n;
        });

        doThrow(new NotificationException("Email delivery failed"))
                .when(emailService).sendEmail(anyString(), anyString(), anyString());

        assertThrows(NotificationException.class, () -> notificationService.sendNotification(request));
        verify(notificationEventProducer).publish(any(NotificationEvent.class));
    }

    @Test
    void sendNotification_smsChannel() {
        SendNotificationRequest request = SendNotificationRequest.builder()
                .userId("user-1")
                .recipientEmail("user@example.com")
                .channel(NotificationChannel.SMS)
                .subject("Test Subject")
                .body("Test Body")
                .eventType("ORDER_CONFIRMED")
                .build();

        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> {
            Notification n = invocation.getArgument(0);
            n.setId(1L);
            return n;
        });

        NotificationDto result = notificationService.sendNotification(request);

        assertNotNull(result);
        assertEquals(NotificationStatus.SENT, result.getStatus());
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(notificationEventProducer).publish(any(NotificationEvent.class));
    }

    @Test
    void getNotificationById_success() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(testNotification));

        NotificationDto result = notificationService.getNotificationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user-1", result.getUserId());
    }

    @Test
    void getNotificationById_notFound() {
        when(notificationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> notificationService.getNotificationById(99L));
    }

    @Test
    void getNotificationsByUserId_success() {
        when(notificationRepository.findByUserIdOrderByCreatedAtDesc("user-1"))
                .thenReturn(List.of(testNotification));

        List<NotificationDto> result = notificationService.getNotificationsByUserId("user-1");

        assertEquals(1, result.size());
        assertEquals("user-1", result.get(0).getUserId());
    }
}
