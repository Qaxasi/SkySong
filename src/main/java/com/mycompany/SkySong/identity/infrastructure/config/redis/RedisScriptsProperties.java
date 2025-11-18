package com.mycompany.SkySong.identity.infrastructure.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "redis.script.session")
public record RedisScriptsProperties(Resource save,
                                     Resource rotate) {
}
