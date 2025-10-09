package com.mycompany.SkySong.identity.infrastructure.authentication.token;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AccessTokenProperties.class, RefreshTokenProperties.class})
class AuthTokenPropsConfig {
}
