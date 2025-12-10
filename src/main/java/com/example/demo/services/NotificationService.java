package com.example.demo.services;

import com.example.demo.models.Notification;
import com.example.demo.models.User;
import java.util.List;

public interface NotificationService {
    Notification createNotification(User recipient, String message);
    List<Notification> getNotificationsByUserId(Long userId);
    List<Notification> getUnreadNotificationsByUserId(Long userId);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
    void sendNotificationToUser(Long userId, String message);
    void sendNotificationToRole(String role, String message);
    void sendNotificationToGroup(Long groupId, String message);
}

