package com.mycompany.SkySong.infrastructure.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "redis.scripts")
public record RedisScriptProperties(Resource save,
                                    Resource rotate) {
}
