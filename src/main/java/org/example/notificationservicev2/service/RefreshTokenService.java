package org.example.notificationservicev2.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "refresh:";
    private static final long REFRESH_TOKEN_EXPIRE_DAYS = 7;

    public String createRefreshToken(Long userID) {
        String refreshToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(
                PREFIX+ userID, refreshToken, REFRESH_TOKEN_EXPIRE_DAYS, TimeUnit.DAYS
        );
        return refreshToken;
    }

    public boolean validateRefreshToken(Long userID, String refreshToken) {
        String storedRedisValue = redisTemplate.opsForValue().get(PREFIX+ userID);
        return storedRedisValue != null && storedRedisValue.equals(refreshToken);
    }

    public void deleteRefreshToken(Long userID) {
        redisTemplate.delete(PREFIX+ userID);
    }
}
