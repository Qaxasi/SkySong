package com.mycompany.SkySong.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
class CryptoConfig {
    @Bean
    SecureRandom secureRandom() {
        return new SecureRandom();
    }
}
