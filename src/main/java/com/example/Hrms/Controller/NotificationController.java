package com.example.Hrms.Controller;

import com.example.Hrms.Entity.Notification;
import com.example.Hrms.Service.NotificationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(
            NotificationService service
    ) {
        this.service = service;
    }

    // GET notifications for logged-in user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(
            @PathVariable Integer userId
    ) {

        return ResponseEntity.ok(
                service.getNotifications(userId)
        );
    }

    // Mark one notification as read
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markRead(
            @PathVariable Integer id
    ) {

        return ResponseEntity.ok(
                service.markRead(id)
        );
    }

    // Mark all notifications as read
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllRead(
            @PathVariable Integer userId
    ) {

        service.markAllRead(userId);

        return ResponseEntity.ok().build();
    }
}