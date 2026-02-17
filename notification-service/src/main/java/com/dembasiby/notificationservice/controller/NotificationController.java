package com.dembasiby.notificationservice.controller;

import com.dembasiby.notificationservice.dto.request.SendNotificationRequest;
import com.dembasiby.notificationservice.dto.response.NotificationDto;
import com.dembasiby.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationDto> sendNotification(@Valid @RequestBody SendNotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificationService.sendNotification(request));
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.getNotificationById(notificationId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<NotificationDto>> getMyNotifications(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }
}
