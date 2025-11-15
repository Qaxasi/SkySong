package com.mycompany.SkySong.identity.infrastructure.authentication.config.token;

import com.mycompany.SkySong.shared.web.cookie.CookieProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AccessTokenProperties.class,
                                SessionProperties.class,
                                AuthCookieProperties.class})
class AuthTokenPropsConfig {
    @Bean("refreshTokenCookieProperties")
    CookieProperties refreshToken(final AuthCookieProperties properties) {
        return properties.refreshToken();
    }

    @Bean("userTagCookieProperties")
    CookieProperties userTag(final AuthCookieProperties properties) {
        return properties.userTag();
    }
}
