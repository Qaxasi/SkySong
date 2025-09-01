package com.mycompany.SkySong.infrastructure.config.redis;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
@EnableConfigurationProperties(RedisScriptProperties.class)
public class RedisScriptsConfig {

    @Bean
    public DefaultRedisScript<Long> rotateRefreshTokenScript(final RedisScriptProperties scriptProperties) {
        final DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(scriptProperties.rotateAndUpdate());
        script.setResultType(Long.class);
        return script;
    }

    @Bean
    public DefaultRedisScript<Long> saveRefreshTokenScript(final RedisScriptProperties scriptProperties) {
        final DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(scriptProperties.save());
        script.setResultType(Long.class);
        return script;
    }

    @Bean
    public DefaultRedisScript<Long> deleteUserSessions(final RedisScriptProperties scriptProperties) {
        final DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(scriptProperties.deleteUserSessions());
        script.setResultType(Long.class);
        return script;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(final RedisConnectionFactory cf) {
        return new StringRedisTemplate(cf);
    }
}
