package com.mycompany.skysong.identity.adapter.out.persistence.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@ConfigurationProperties(prefix = "redis.script.session")
public record RedisSessionScriptsProperties(Resource save,
                                            Resource rotate) {
}
