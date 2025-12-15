package com.mycompany.skysong.identity.application.user.authentication.service;

import com.mycompany.skysong.identity.application.user.authentication.model.AccessGrant;
import com.mycompany.skysong.identity.application.user.authentication.model.AccessToken;
import com.mycompany.skysong.identity.application.user.authentication.model.AccessTokenClaims;
import com.mycompany.skysong.identity.application.user.authentication.port.*;
import com.mycompany.skysong.identity.domain.RawPassword;
import com.mycompany.skysong.identity.domain.Session;
import com.mycompany.skysong.identity.domain.Username;
import com.mycompany.skysong.core.result.Result;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class LoginUser {
    private final UserCredentialsAuthenticator authenticator;
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final SessionStore sessionStore;
    private final UserAccessSnapshotReader userAccessSnapshotReader;
    private final Clock clock;
    private final Duration sessionLifetime;

    public LoginUser(final UserCredentialsAuthenticator authenticator,
                     final AccessTokenGenerator accessTokenGenerator,
                     final RefreshTokenGenerator refreshTokenGenerator,
                     final SessionStore sessionStore,
                     final UserAccessSnapshotReader userAccessSnapshotReader,
                     final Clock clock,
                     final Duration sessionLifetime) {
        this.authenticator = authenticator;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.sessionStore = sessionStore;
        this.userAccessSnapshotReader = userAccessSnapshotReader;
        this.clock = clock;
        this.sessionLifetime = sessionLifetime;
    }

    public Result<AccessGrant> login(final Username username, final RawPassword password) {
        try (password) {
            final Instant now = clock.instant();

            return authenticator.authenticate(username, password)
                    .flatMap(user -> userAccessSnapshotReader.read(user.id())
                            .flatMap(userAccessSnapshot -> Session.issue(
                                            user.id(),
                                            now,
                                            sessionLifetime)
                                    .flatMap(session ->
                                            refreshTokenGenerator.generate()
                                                    .flatMap(refreshToken -> {
                                                        final Duration sessionTtl = session.ttl(now);
                                                        return sessionStore.saveSession(
                                                                        userAccessSnapshot.userTag(),
                                                                        refreshToken,
                                                                        session,
                                                                        sessionTtl)
                                                                .map(ignored -> {
                                                                    final AccessTokenClaims claims = userAccessSnapshot.toClaims(session.userId());
                                                                    final AccessToken accessToken = accessTokenGenerator.generate(claims);

                                                                    final long expiresIn = accessToken.expiresInSeconds(now);
                                                                    return new AccessGrant(
                                                                            accessToken,
                                                                            expiresIn,
                                                                            refreshToken,
                                                                            sessionTtl,
                                                                            userAccessSnapshot.userTag());
                                                                });
                                                    })
                                    )
                            )
                    );
        }
    }
}