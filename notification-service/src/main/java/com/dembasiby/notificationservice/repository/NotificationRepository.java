package com.dembasiby.notificationservice.repository;

import com.dembasiby.notificationservice.model.Notification;
import com.dembasiby.notificationservice.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Notification> findByStatus(NotificationStatus status);
}
