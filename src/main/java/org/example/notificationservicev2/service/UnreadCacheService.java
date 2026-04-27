package org.example.notificationservicev2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnreadCacheService {
    private final StringRedisTemplate redisTemplate;
    private static final String PREFIX = "unread:";

    public void increment(Long userId){
        redisTemplate.opsForValue().increment(PREFIX + userId);
    }

    public void decrement(Long userId){
        String key = PREFIX + userId;
        Long val = redisTemplate.opsForValue().decrement(key);
        //Don't go below 0
        if(val != null && val < 0){
            redisTemplate.opsForValue().set(key, "0");
        }
    }

    public long getUnreadCount(Long userId){
        String val = redisTemplate.opsForValue().get(PREFIX + userId);
        if (val == null) {
            return 0;
        } else {
            return Long.parseLong(val);
        }
    }

    public void reset(Long userId) {
        redisTemplate.opsForValue().set(PREFIX + userId, "0");
    }

}
