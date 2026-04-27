package org.example.notificationservicev2.kafka;

import lombok.RequiredArgsConstructor;
import org.example.notificationservicev2.dto.NotificationEventDto;
import org.example.notificationservicev2.service.RateLimiterService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer { //REST → Kafka

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RateLimiterService rateLimiterService;

    public String sendNotification(NotificationEventDto event) {
        if(!rateLimiterService.isAllowed(event.getUserID())){
            return "Rate limit reached. Max 5 notification per minute.";
        }
        kafkaTemplate.send(KafkaTopicConfig.NOTIFICATION_TOPIC, event);
        return "Notification queued successfully.";
    }

}
