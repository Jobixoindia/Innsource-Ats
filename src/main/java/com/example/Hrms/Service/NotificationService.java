package com.example.Hrms.Service;

import com.example.Hrms.Entity.Notification;
import com.example.Hrms.Repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(
            NotificationRepository repository
    ) {
        this.repository = repository;
    }

    // Get notifications for one user
    public List<Notification> getNotifications(
            Integer userId
    ) {
        return repository
                .findByRecipientIdOrderByCreatedAtDesc(userId);
    }

    // Create notification
    public Notification createNotification(
            Integer recipientId,
            Integer requestId,
            String kind,
            String title,
            String body
    ) {

        Notification notification = new Notification();

        notification.setRecipientId(recipientId);
        notification.setRequestId(requestId);
        notification.setKind(kind);
        notification.setTitle(title);
        notification.setBody(body);
        notification.setUnread(true);
        notification.setCreatedAt(LocalDateTime.now());

        return repository.save(notification);
    }

    // Mark one notification as read
    public Notification markRead(Integer id) {

        Notification notification =
                repository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Notification not found"
                                )
                        );

        notification.setUnread(false);

        return repository.save(notification);
    }

    // Mark all notifications for user as read
    public void markAllRead(Integer userId) {

        List<Notification> notifications =
                repository.findByRecipientIdOrderByCreatedAtDesc(
                        userId
                );

        for (Notification notification : notifications) {
            notification.setUnread(false);
        }

        repository.saveAll(notifications);
    }
}