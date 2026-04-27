package org.example.notificationservicev2.controller;

import lombok.RequiredArgsConstructor;

import org.example.notificationservicev2.dto.NotificationEventDto;
import org.example.notificationservicev2.entity.Notification;

import org.example.notificationservicev2.kafka.NotificationProducer;
import org.example.notificationservicev2.service.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationProducer notificationProducer;
    private final NotificationService notificationService;

    // Publish a notification to Kafka
    @PostMapping("/send")
    public String send(@RequestBody NotificationEventDto event) {
        return notificationProducer.sendNotification(event);
    }

    // Get all notifications for a user
    @GetMapping("/my/{userId}")
    public List<Notification> getMyNotifications(@PathVariable Long userId) {
        return notificationService.getMyNotifications(userId);
    }

    // Mark a notification as read
    @PatchMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id, @RequestParam Long userId) {
        return notificationService.markAsRead(id, userId);
    }

    // Get unread count from Redis cache
    @GetMapping("/unread-count/{userId}")
    public long getUnreadCount(@PathVariable Long userId) {
        return notificationService.getUnreadCount(userId);
    }

    // Get all failed notifications (landed in DLQ)
    @GetMapping("/failed")
    public List<Notification> getFailed() {
        return notificationService.getFailedNotifications();
    }

    // Get all failed notifications (landed in DLQ)
    @GetMapping("/hello")
    public String getHello() {
        return "Hello!";
    }
}