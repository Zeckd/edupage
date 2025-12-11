package com.example.demo.controllers;

import com.example.demo.models.Notification;
import com.example.demo.models.Student;
import com.example.demo.models.User;
import com.example.demo.models.dtos.NotificationRequestDto;
import com.example.demo.repositories.StudentRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @GetMapping
    public ResponseEntity<List<Notification>> getMyNotifications(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(user.getId()));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByUserId(user.getId()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Map<String, String>> sendNotification(@RequestBody NotificationRequestDto dto) {
        if (dto.getUserId() != null) {
            notificationService.sendNotificationToUser(dto.getUserId(), dto.getMessage());
            return ResponseEntity.ok(Map.of("message", "Уведомление отправлено пользователю"));
        } else if (dto.getRole() != null) {
            notificationService.sendNotificationToRole(dto.getRole(), dto.getMessage());
            return ResponseEntity.ok(Map.of("message", "Уведомление отправлено всем " + dto.getRole()));
        } else if (dto.getGroupId() != null) {
            notificationService.sendNotificationToGroup(dto.getGroupId(), dto.getMessage());
            return ResponseEntity.ok(Map.of("message", "Уведомление отправлено группе"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Необходимо указать userId, role или groupId"));
        }
    }
}

