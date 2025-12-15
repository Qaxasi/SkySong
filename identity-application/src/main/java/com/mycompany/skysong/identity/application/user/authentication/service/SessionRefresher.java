package com.mycompany.skysong.identity.application.user.authentication.service;

import com.mycompany.skysong.identity.application.user.authentication.model.AuthGrant;
import com.mycompany.skysong.identity.application.user.authentication.model.AccessToken;
import com.mycompany.skysong.identity.application.user.authentication.model.AccessTokenClaims;
import com.mycompany.skysong.identity.application.user.authentication.port.*;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.identity.domain.UserTag;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class SessionRefresher {
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final UserAccessSnapshotReader userAccessSnapshotReader;
    private final SessionStore sessionStore;
    private final Clock clock;
    private final Duration sessionLifetime;

    public SessionRefresher(final AccessTokenGenerator accessTokenGenerator,
                            final RefreshTokenGenerator refreshTokenGenerator,
                            final UserAccessSnapshotReader userAccessSnapshotReader,
                            final SessionStore sessionStore,
                            final Clock clock,
                            final Duration sessionLifetime) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.userAccessSnapshotReader = userAccessSnapshotReader;
        this.sessionStore = sessionStore;
        this.clock = clock;
        this.sessionLifetime = sessionLifetime;
    }

    public Result<AuthGrant> refresh(final RefreshToken oldRefreshToken, final UserTag userTag) {
        final Instant now = clock.instant();

        return sessionStore.findSession(userTag, oldRefreshToken)
                .flatMap(session -> session.reissueIfActive(now, sessionLifetime)
                        .flatMap(newSession -> userAccessSnapshotReader.read(newSession.userId())
                                .flatMap(userAccessSnapshot -> refreshTokenGenerator.generate()
                                        .flatMap(newRefreshToken -> {
                                            final Duration sessionTtl = newSession.ttl(now);

                                            return sessionStore.rotateSession(
                                                            userAccessSnapshot.userTag(),
                                                            oldRefreshToken,
                                                            newRefreshToken,
                                                            newSession,
                                                            sessionTtl)
                                                    .map(ignored -> {
                                                        final AccessTokenClaims claims = userAccessSnapshot.toClaims(newSession.userId());
                                                        final AccessToken newAt = accessTokenGenerator.generate(claims);
                                                        return new AuthGrant(
                                                                newAt,
                                                                newAt.expiresInSeconds(now),
                                                                newRefreshToken,
                                                                sessionTtl,
                                                                userAccessSnapshot.userTag());
                                                    });
                                        })
                                )
                        )
                );
    }
}

