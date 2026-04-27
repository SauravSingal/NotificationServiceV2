package org.example.notificationservicev2.kafka;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String NOTIFICATION_TOPIC = "notifications";
    public static final String NOTIFICATION_DLQ_TOPIC = "notifications.DLQ";

    @Bean
    public NewTopic notificationsTopic(){
        return TopicBuilder.name(NOTIFICATION_TOPIC)
                .build();
    }

    @Bean
    public NewTopic notificationDLQTopic(){
        return TopicBuilder.name(NOTIFICATION_DLQ_TOPIC)
                .partitions(1).replicas(1).build();
    }

}
