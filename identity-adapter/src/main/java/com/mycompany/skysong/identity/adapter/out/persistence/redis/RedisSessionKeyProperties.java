package com.mycompany.skysong.identity.adapter.out.persistence.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("redis.session")
public record RedisSessionKeyProperties(
        String namespace,
        int version) {
}
