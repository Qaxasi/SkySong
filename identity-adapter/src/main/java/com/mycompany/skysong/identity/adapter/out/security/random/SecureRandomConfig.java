package com.mycompany.skysong.identity.adapter.out.security.random;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
class SecureRandomConfig {
    @Bean
    SecureRandom secureRandom() {
        return new SecureRandom();
    }
}
