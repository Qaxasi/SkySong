package com.mycompany.SkySong.infrastructure.config.redis;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RedisSessionKeyProperties.class)
public class RedisSessionKeyConfig {
}
