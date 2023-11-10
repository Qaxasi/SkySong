package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.model.entity.BlacklistedToken;
import com.mycompany.SkySong.authentication.service.TokenStoreService;
import org.springframework.data.redis.core.RedisTemplate;

public class TokenStoreServiceImpl implements TokenStoreService {
    private final RedisTemplate<String, String> redisTemplate;
    public TokenStoreServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void blacklistToken(String token) {
        if (!blacklistedTokenDAO.existsByToken(token)) {
            BlacklistedToken blacklistedToken = new BlacklistedToken();
            blacklistedToken.setToken(token);
            blacklistedTokenDAO.save(blacklistedToken);
        }
    }
}
