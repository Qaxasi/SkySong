package com.mycompany.skysong.identity.adapter.out.persistence.redis;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
@EnableConfigurationProperties({
        RedisSessionScriptsProperties.class,
        RedisSessionKeyProperties.class})
public class RedisSessionConfig {
    @Bean
    public DefaultRedisScript<Long> rotateSession(final RedisSessionScriptsProperties properties) {
        final DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(properties.rotate());
        script.setResultType(Long.class);
        return script;
    }

    @Bean
    public DefaultRedisScript<Long> saveSession(final RedisSessionScriptsProperties properties) {
        final DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(properties.save());
        script.setResultType(Long.class);
        return script;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(final RedisConnectionFactory cf) {
        return new StringRedisTemplate(cf);
    }
}
