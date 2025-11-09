package com.mycompany.SkySong.identity.infrastructure.authentication.config.token;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({AccessTokenProperties.class, SessionProperties.class})
class AuthTokenPropsConfig {
}
