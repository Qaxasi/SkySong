package com.mycompany.SkySong.identity.authentication.application.service;

import com.mycompany.SkySong.identity.authentication.application.dto.AccessGrant;
import com.mycompany.SkySong.identity.authentication.application.dto.AccessToken;
import com.mycompany.SkySong.identity.authentication.application.dto.AccessTokenClaims;
import com.mycompany.SkySong.identity.authentication.application.port.UserAccessSnapshotReader;
import com.mycompany.SkySong.identity.authentication.application.port.AccessTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.port.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.port.SessionStore;
import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.identity.authentication.domain.UserTag;
import com.mycompany.SkySong.identity.infrastructure.authentication.config.token.SessionProperties;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class TokenPairRefresher {
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final UserAccessSnapshotReader snapshotReader;
    private final SessionStore sessionStore;
    private final Clock clock;
    private final Duration ttl;

    public TokenPairRefresher(final AccessTokenGenerator accessTokenGenerator,
                              final RefreshTokenGenerator refreshTokenGenerator,
                              final UserAccessSnapshotReader snapshotReader,
                              final SessionStore sessionStore,
                              final Clock clock,
                              final SessionProperties properties) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.snapshotReader = snapshotReader;
        this.sessionStore = sessionStore;
        this.clock = clock;
        this.ttl = properties.ttl();
    }

    public Result<AccessGrant> refresh(final RefreshToken oldRt, final UserTag userTag) {
        final Instant now = clock.instant();

        return sessionStore.findBy(userTag, oldRt)
                        .flatMap(session ->
                            snapshotReader.load(session.userId())
                                    .flatMap(snapshot -> {
                                        if (session.isAccessVersionOutdated(snapshot.accessVersion())) {
                                            return Result.failure("Session outdated", ErrorType.INVALID_SESSION);
                                        }
                                        if (session.isExpired(now)) {
                                            return Result.failure("Session expired", ErrorType.INVALID_SESSION);
                                        }
                                        return session.reissue(now, ttl, snapshot.accessVersion())
                                                .flatMap(newSession ->
                                                        refreshTokenGenerator.generate()
                                                                .flatMap(newRt -> {
                                                                    final Duration sessionTtl = newSession.ttl(now);

                                                                    return sessionStore.refresh(
                                                                                    snapshot.userTag(),
                                                                                    oldRt,
                                                                                    newRt,
                                                                                    session,
                                                                                    sessionTtl)
                                                                            .map(ignored -> {
                                                                                final AccessTokenClaims claims = new AccessTokenClaims(
                                                                                        session.userId(),
                                                                                        snapshot.roles(),
                                                                                        newSession.accessVersionAtIssue());
                                                                                final AccessToken newAt = accessTokenGenerator.generate(claims);
                                                                                return new AccessGrant(
                                                                                        newAt,
                                                                                        newAt.expiresInSeconds(now),
                                                                                        newRt,
                                                                                        sessionTtl,
                                                                                        userTag);
                                                                            });
                                                                })
                                                );
                                    })
                        );
    }
}
