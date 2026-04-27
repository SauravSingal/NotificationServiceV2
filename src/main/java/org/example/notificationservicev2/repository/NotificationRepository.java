package org.example.notificationservicev2.repository;

import org.example.notificationservicev2.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    List<Notification> findByStatus(String status);
    long countByUserIdAndReadFalse(Long userId);
}
