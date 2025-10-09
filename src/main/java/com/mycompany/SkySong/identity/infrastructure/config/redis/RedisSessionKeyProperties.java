package com.mycompany.SkySong.infrastructure.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis.session")
public record RedisSessionKeyProperties(
        String namespace,
        int version) {
}
