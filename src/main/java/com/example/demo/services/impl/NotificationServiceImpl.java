package com.example.demo.services.impl;

import com.example.demo.models.Notification;
import com.example.demo.models.Student;
import com.example.demo.models.User;
import com.example.demo.repositories.NotificationRepository;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public Notification createNotification(User recipient, String message) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .message(message)
                .createdAt(LocalDateTime.now())
                .readFlag(false)
                .build();
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> getUnreadNotificationsByUserId(Long userId) {
        return notificationRepository.findByRecipientIdAndReadFlagFalseOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setReadFlag(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByRecipientIdAndReadFlagFalseOrderByCreatedAtDesc(userId);
        notifications.forEach(n -> n.setReadFlag(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    @Transactional
    public void sendNotificationToUser(Long userId, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Notification notification = createNotification(user, message);
        
        messagingTemplate.convertAndSend("/queue/notifications/" + userId, notification);
    }

    @Override
    @Transactional
    public void sendNotificationToRole(String role, String message) {
        List<User> users = userRepository.findByRole(com.example.demo.enums.Role.valueOf(role));
        
        for (User user : users) {
            Notification notification = createNotification(user, message);
            messagingTemplate.convertAndSend("/queue/notifications/" + user.getId(), notification);
        }
        
        messagingTemplate.convertAndSend("/topic/notifications/" + role.toLowerCase(), message);
    }

    @Override
    @Transactional
    public void sendNotificationToGroup(Long groupId, String message) {
        List<Student> students = studentRepository.findByGroupId(groupId);
        
        for (Student student : students) {
            Notification notification = createNotification(student.getUser(), message);
            messagingTemplate.convertAndSend("/queue/notifications/" + student.getUser().getId(), notification);
        }
    }
}

