package org.example.notificationservicev2.service;


import lombok.RequiredArgsConstructor;
import org.example.notificationservicev2.entity.Notification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimiterService {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "rate:";
    private static final int MAX_REQUESTS = 5;
    private static final long EXPIRES_IN_SECONDS = 60;

    public boolean isAllowed(Long userId) {
        String key = PREFIX + userId;
        Long count = redisTemplate.opsForValue().increment(key);

        if(count == 1){
            redisTemplate.expire(key, EXPIRES_IN_SECONDS, TimeUnit.SECONDS);
        }

        return count <= MAX_REQUESTS;
    }
}
