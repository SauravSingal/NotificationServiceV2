package org.example.notificationservicev2.service;

import lombok.RequiredArgsConstructor;
import org.example.notificationservicev2.entity.Notification;
import org.example.notificationservicev2.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UnreadCacheService unreadCacheService;

    public List<Notification> getMyNotifications(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getFailedNotifications() {
        return notificationRepository.findByStatus("FAILED");
    }

    public String markAsRead(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
            unreadCacheService.decrement(userId);
        }

        return "Marked as read";
    }

    public long getUnreadCount(Long userId) {
        return unreadCacheService.getUnreadCount(userId);
    }
}