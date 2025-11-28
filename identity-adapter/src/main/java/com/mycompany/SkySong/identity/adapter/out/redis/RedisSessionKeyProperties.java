package com.mycompany.SkySong.identity.a.adapter.out.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis.session")
public record RedisSessionKeyProperties(
        String namespace,
        int version) {
}
