package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.model.entity.BlacklistedToken;
import com.mycompany.SkySong.authentication.service.TokenStoreService;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class TokenStoreServiceImpl implements TokenStoreService {
    private final RedisTemplate<String, String> redisTemplate;
    public TokenStoreServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void blacklistToken(String token) {
        redisTemplate.opsForValue().set(token, "blacklisted", 15, TimeUnit.MINUTES);
    }
    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
