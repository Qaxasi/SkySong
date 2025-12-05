package com.mycompany.SkySong.identity.config;

import com.mycompany.SkySong.identity.application.port.*;
import com.mycompany.SkySong.identity.application.service.SessionRefresher;
import com.mycompany.SkySong.identity.application.service.UserAuthenticator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties({AccessTokenProperties.class, SessionProperties.class})
class AuthenticationConfig {
    @Bean
    UserAuthenticator userAuthenticator(final Authenticator authenticator,
                                        final AccessTokenGenerator accessTokenGenerator,
                                        final RefreshTokenGenerator refreshTokenGenerator,
                                        final SessionStore sessionStore,
                                        final UserAccessSnapshotReader snapshotReader,
                                        final Clock clock,
                                        final SessionProperties sessionProperties) {
        return new UserAuthenticator(
                authenticator,
                accessTokenGenerator,
                refreshTokenGenerator,
                sessionStore,
                snapshotReader,
                clock,
                sessionProperties.ttl());
    }

    @Bean
    SessionRefresher sessionRefresher(final AccessTokenGenerator accessTokenGenerator,
                                      final RefreshTokenGenerator refreshTokenGenerator,
                                      final UserAccessSnapshotReader snapshotReader,
                                      final SessionStore sessionStore,
                                      final Clock clock,
                                      final SessionProperties sessionProperties) {
        return new SessionRefresher(
                accessTokenGenerator,
                refreshTokenGenerator,
                snapshotReader,
                sessionStore,
                clock,
                sessionProperties.ttl());

    }
}
