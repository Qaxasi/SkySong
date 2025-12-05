package com.mycompany.skysong.identity.application.authentication.service;

import com.mycompany.skysong.identity.application.authentication.model.AccessGrant;
import com.mycompany.skysong.identity.application.authentication.model.AccessToken;
import com.mycompany.skysong.identity.application.authentication.model.AccessTokenClaims;
import com.mycompany.skysong.identity.application.authentication.port.AccessTokenGenerator;
import com.mycompany.skysong.identity.application.authentication.port.RefreshTokenGenerator;
import com.mycompany.skysong.identity.application.authentication.port.SessionStore;
import com.mycompany.skysong.identity.application.authentication.port.UserAccessSnapshotReader;
import com.mycompany.skysong.core.error.ErrorType;
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
    private final Duration ttl;

    public SessionRefresher(final AccessTokenGenerator accessTokenGenerator,
                            final RefreshTokenGenerator refreshTokenGenerator,
                            final UserAccessSnapshotReader snapshotReader,
                            final SessionStore sessionStore,
                            final Clock clock,
                            final Duration ttl) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.snapshotReader = snapshotReader;
        this.sessionStore = sessionStore;
        this.clock = clock;
        this.ttl = ttl;
    }

    public Result<AccessGrant> refresh(final RefreshToken oldRefreshToken, final UserTag userTag) {
        final Instant now = clock.instant();

        return sessionStore.findBy(userTag, oldRefreshToken)
                        .flatMap(session ->
                            snapshotReader.load(session.userId())
                                    .flatMap(userAccessSnapshot -> {
                                        if (session.isExpired(now)) {
                                            return Result.failure("Session expired", ErrorType.INVALID_SESSION);
                                        }
                                        return session.reissue(now, ttl)
                                                .flatMap(newSession ->
                                                        refreshTokenGenerator.generate()
                                                                .flatMap(newRefreshToken -> {
                                                                    final Duration sessionTtl = newSession.ttl(now);

                                                                    return sessionStore.rotateSession(
                                                                                    userAccessSnapshot.userTag(),
                                                                                    oldRefreshToken,
                                                                                    newRefreshToken,
                                                                                    newSession,
                                                                                    sessionTtl)
                                                                            .map(ignored -> {
                                                                                final AccessTokenClaims claims = new AccessTokenClaims(
                                                                                        newSession.userId(),
                                                                                        userAccessSnapshot.roles());
                                                                                final AccessToken newAt = accessTokenGenerator.generate(claims);
                                                                                return new AccessGrant(
                                                                                        newAt,
                                                                                        newAt.expiresInSeconds(now),
                                                                                        newRefreshToken,
                                                                                        sessionTtl,
                                                                                        userAccessSnapshot.userTag());
                                                                            });
                                                                })
                                                );
                                    })
                        );
    }
}
