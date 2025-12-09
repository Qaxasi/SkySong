package com.mycompany.skysong.app.config.identity;

import com.mycompany.skysong.app.config.identity.properties.AuthCookieProperties;
import com.mycompany.skysong.app.config.identity.properties.CookieProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AuthCookieProperties.class)
class AuthCookieConfig {
    @Bean("refreshTokenCookieProperties")
    CookieProperties refreshToken(final AuthCookieProperties properties) {
        return properties.refreshToken();
    }

    @Bean("userTagCookieProperties")
    CookieProperties userTag(final AuthCookieProperties properties) {
        return properties.userTag();
    }
}
