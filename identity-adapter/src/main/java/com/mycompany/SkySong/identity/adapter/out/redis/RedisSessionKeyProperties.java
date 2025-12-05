package com.mycompany.SkySong.identity.adapter.out.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis.session")
public record RedisSessionKeyProperties(
        String namespace,
        int version) {
}
