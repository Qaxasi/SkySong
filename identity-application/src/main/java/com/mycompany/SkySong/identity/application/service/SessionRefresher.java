package com.mycompany.SkySong.identity.application.service;

import com.mycompany.SkySong.identity.application.dto.AccessGrant;
import com.mycompany.SkySong.identity.application.dto.AccessToken;
import com.mycompany.SkySong.identity.application.dto.AccessTokenClaims;
import com.mycompany.SkySong.identity.application.port.AccessTokenGenerator;
import com.mycompany.SkySong.identity.application.port.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.application.port.SessionStore;
import com.mycompany.SkySong.identity.application.port.UserAccessSnapshotReader;
import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.identity.domain.UserTag;
import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;

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

    public Result<AccessGrant> refresh(final RefreshToken oldRt, final UserTag userTag) {
        final Instant now = clock.instant();

        return sessionStore.findBy(userTag, oldRt)
                        .flatMap(session ->
                            snapshotReader.load(session.userId())
                                    .flatMap(snapshot -> {
                                        if (session.isAccessVersionOutdated(snapshot.accessVersion())) {
                                            return Result.failure("Session revoked", ErrorType.INVALID_SESSION);
                                        }
                                        if (session.isExpired(now)) {
                                            return Result.failure("Session expired", ErrorType.INVALID_SESSION);
                                        }
                                        return session.reissue(now, ttl, snapshot.accessVersion())
                                                .flatMap(newSession ->
                                                        refreshTokenGenerator.generate()
                                                                .flatMap(newRt -> {
                                                                    final Duration sessionTtl = newSession.ttl(now);

                                                                    return sessionStore.rotateSession(
                                                                                    snapshot.userTag(),
                                                                                    oldRt,
                                                                                    newRt,
                                                                                    newSession,
                                                                                    sessionTtl)
                                                                            .map(ignored -> {
                                                                                final AccessTokenClaims claims = new AccessTokenClaims(
                                                                                        newSession.userId(),
                                                                                        snapshot.roles(),
                                                                                        newSession.accessVersionAtIssue());
                                                                                final AccessToken newAt = accessTokenGenerator.generate(claims);
                                                                                return new AccessGrant(
                                                                                        newAt,
                                                                                        newAt.expiresInSeconds(now),
                                                                                        newRt,
                                                                                        sessionTtl,
                                                                                        snapshot.userTag());
                                                                            });
                                                                })
                                                );
                                    })
                        );
    }
}
