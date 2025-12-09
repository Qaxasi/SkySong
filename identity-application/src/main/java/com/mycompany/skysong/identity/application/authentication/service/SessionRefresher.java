package com.mycompany.skysong.identity.application.authentication.service;

import com.mycompany.skysong.identity.application.authentication.model.AccessGrant;
import com.mycompany.skysong.identity.application.authentication.model.AccessToken;
import com.mycompany.skysong.identity.application.authentication.model.AccessTokenClaims;
import com.mycompany.skysong.identity.application.authentication.port.AccessTokenGenerator;
import com.mycompany.skysong.identity.application.authentication.port.RefreshTokenGenerator;
import com.mycompany.skysong.identity.application.authentication.port.SessionStore;
import com.mycompany.skysong.identity.application.authentication.port.UserAccessSnapshotReader;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.identity.domain.UserTag;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class SessionRefresher {
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final UserAccessSnapshotReader snapshotReader;
    private final SessionStore sessionStore;
    private final Clock clock;
    private final Duration sessionLifetime;

    public SessionRefresher(final AccessTokenGenerator accessTokenGenerator,
                            final RefreshTokenGenerator refreshTokenGenerator,
                            final UserAccessSnapshotReader snapshotReader,
                            final SessionStore sessionStore,
                            final Clock clock,
                            final Duration sessionLifetime) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.snapshotReader = snapshotReader;
        this.sessionStore = sessionStore;
        this.clock = clock;
        this.sessionLifetime = sessionLifetime;
    }

    public Result<AccessGrant> refresh(final RefreshToken oldRefreshToken, final UserTag userTag) {
        final Instant now = clock.instant();

        return sessionStore.findBy(userTag, oldRefreshToken)
                .flatMap(session -> session.reissueIfActive(now, sessionLifetime)
                        .flatMap(newSession -> snapshotReader.load(newSession.userId())
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
                                                        return new AccessGrant(
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

