package com.example.Hrms.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Hrms.Entity.Notification;

public interface NotificationRepository
extends JpaRepository<Notification, Integer> {

List<Notification> findByRecipientIdOrderByCreatedAtDesc(
    Integer recipientId
);

List<Notification> findByRequestId(
    Integer requestId
);
}