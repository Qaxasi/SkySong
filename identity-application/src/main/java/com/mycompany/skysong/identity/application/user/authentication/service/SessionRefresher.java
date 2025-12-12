package com.mycompany.skysong.identity.application.user.authentication.service;

import com.mycompany.skysong.identity.application.user.authentication.model.AccessGrant;
import com.mycompany.skysong.identity.application.user.authentication.model.AccessToken;
import com.mycompany.skysong.identity.application.user.authentication.model.AccessTokenClaims;
import com.mycompany.skysong.identity.application.user.authentication.port.AccessTokenGenerator;
import com.mycompany.skysong.identity.application.user.authentication.port.RefreshTokenGenerator;
import com.mycompany.skysong.identity.application.user.authentication.port.SessionStore;
import com.mycompany.skysong.identity.application.user.authentication.port.UserAccessSnapshotQuery;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.identity.domain.UserTag;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class SessionRefresher {
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final UserAccessSnapshotQuery userAccessSnapshotQuery;
    private final SessionStore sessionStore;
    private final Clock clock;
    private final Duration sessionLifetime;

    public SessionRefresher(final AccessTokenGenerator accessTokenGenerator,
                            final RefreshTokenGenerator refreshTokenGenerator,
                            final UserAccessSnapshotQuery userAccessSnapshotQuery,
                            final SessionStore sessionStore,
                            final Clock clock,
                            final Duration sessionLifetime) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.userAccessSnapshotQuery = userAccessSnapshotQuery;
        this.sessionStore = sessionStore;
        this.clock = clock;
        this.sessionLifetime = sessionLifetime;
    }

    public Result<AccessGrant> refresh(final RefreshToken oldRefreshToken, final UserTag userTag) {
        final Instant now = clock.instant();

        return sessionStore.findBy(userTag, oldRefreshToken)
                .flatMap(session -> session.reissueIfActive(now, sessionLifetime)
                        .flatMap(newSession -> userAccessSnapshotQuery.fetch(newSession.userId())
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

