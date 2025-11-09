package com.mycompany.SkySong.identity.infrastructure.authentication.config.token;

import com.mycompany.SkySong.shared.web.cookie.CookieProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AccessTokenProperties.class, SessionProperties.class})
class AuthTokenPropsConfig {
    @Bean
    CookieProperties cookieProperties(final SessionProperties properties) {
        return properties.cookie();
    }
}
