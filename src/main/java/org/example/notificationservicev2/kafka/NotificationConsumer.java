package org.example.notificationservicev2.kafka;

import lombok.RequiredArgsConstructor;
import org.example.notificationservicev2.dto.NotificationEventDto;
import org.example.notificationservicev2.entity.Notification;
import org.example.notificationservicev2.repository.NotificationRepository;
import org.example.notificationservicev2.service.UnreadCacheService;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.BackOff;

@Service
@RequiredArgsConstructor
public class NotificationConsumer { //Kafka → DB + Redis //Listener

    private final NotificationRepository notificationRepository;
    private final UnreadCacheService unreadCacheService;

    @RetryableTopic(
            attempts = "4",
            backOff = @BackOff(delay = 2000),
            dltTopicSuffix = ".DLQ"
    )
    @KafkaListener(
            topics = KafkaTopicConfig.NOTIFICATION_TOPIC,
            groupId = "notification-group"
    )
    public void listenOrConsume(NotificationEventDto event) {

        //only to test failure scenarios - retries for even users
        if(event.getUserID() % 2 == 0) {
            throw new RuntimeException("Simulated processing failure");
        }

        Notification notification = Notification.builder()
                .userId(event.getUserID())
                .type(event.getType())
                .title(event.getTitle())
                .message(event.getMessage())
                .status("Delivered")
                .read(false)
                .build();
        notificationRepository.save(notification);
        unreadCacheService.increment(event.getUserID());
    }


    //DLQ listener- as Kafka is even driven this will execute as soon as there is message in this DLQ queue
    @DltHandler
    public void consume(NotificationEventDto event) {

        Notification notification = Notification.builder()
                .userId(event.getUserID())
                .type(event.getType())
                .title(event.getTitle())
                .message(event.getMessage())
                .status("FAILED")
                .read(false)
                .build();

        notificationRepository.save(notification);
    }
}
