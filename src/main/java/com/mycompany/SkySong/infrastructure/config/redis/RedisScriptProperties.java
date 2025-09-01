package com.mycompany.SkySong.infrastructure.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "redis.session.script")
public record RedisScriptProperties(Resource save,
                                    Resource rotateAndUpdate,
                                    Resource deleteUserSessions) {
}
