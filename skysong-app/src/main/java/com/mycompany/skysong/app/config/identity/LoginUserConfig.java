package com.mycompany.skysong.app.config.identity;

import com.mycompany.skysong.identity.application.user.authentication.port.*;
import com.mycompany.skysong.identity.application.user.authentication.service.SessionRefresher;
import com.mycompany.skysong.identity.application.user.authentication.service.LoginUser;
import com.mycompany.skysong.app.config.identity.properties.SessionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class LoginUserConfig {
    @Bean
    LoginUser login(final Authenticator authenticator,
                    final AccessTokenGenerator accessTokenGenerator,
                    final RefreshTokenGenerator refreshTokenGenerator,
                    final SessionStore sessionStore,
                    final UserAccessSnapshotReader snapshotReader,
                    final Clock clock,
                    final SessionProperties sessionProperties) {
        return new LoginUser(
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
