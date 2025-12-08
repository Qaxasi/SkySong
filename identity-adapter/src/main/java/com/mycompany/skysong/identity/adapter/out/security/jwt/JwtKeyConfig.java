package com.mycompany.skysong.identity.adapter.out.security.jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@EnableConfigurationProperties(JwtKeyProperties.class)
class JwtKeyConfig {
    @Bean("jwtSigningKey")
    SecretKey jwtSigningKey(final JwtKeyProperties properties) {
        final byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64URL.decode(properties.secretKey());
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("Invalid Base64 for JWT secret", ex);
        }
        if (keyBytes.length < 32) {
            throw new IllegalStateException("Jwt secret to short for HS256");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
