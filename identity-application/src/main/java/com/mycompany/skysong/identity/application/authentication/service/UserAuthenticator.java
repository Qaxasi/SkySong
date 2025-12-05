package com.mycompany.skysong.identity.application.authentication.service;

import com.mycompany.skysong.identity.application.authentication.model.AccessGrant;
import com.mycompany.skysong.identity.application.authentication.model.AccessToken;
import com.mycompany.skysong.identity.application.authentication.model.AccessTokenClaims;
import com.mycompany.skysong.identity.application.authentication.port.*;
import com.mycompany.skysong.identity.domain.RawPassword;
import com.mycompany.skysong.identity.domain.Session;
import com.mycompany.skysong.identity.domain.Username;
import com.mycompany.skysong.core.result.Result;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

public class UserAuthenticator {
    private final Authenticator authenticator;
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final SessionStore sessionStore;
    private final UserAccessSnapshotReader snapshotReader;
    private final Clock clock;
    private final Duration ttl;

    public UserAuthenticator(final Authenticator authenticator,
                             final AccessTokenGenerator accessTokenGenerator,
                             final RefreshTokenGenerator refreshTokenGenerator,
                             final SessionStore sessionStore,
                             final UserAccessSnapshotReader snapshotReader,
                             final Clock clock,
                             final Duration ttl) {
        this.authenticator = authenticator;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.sessionStore = sessionStore;
        this.snapshotReader = snapshotReader;
        this.clock = clock;
        this.ttl = ttl;
    }


    public Result<AccessGrant> login(final Username username, final RawPassword password) {
       try (password) {
           return authenticator.authenticate(username, password)
                   .flatMap(user -> snapshotReader.load(user.id())
                           .flatMap(userAccessSnapshot -> {
                               final Instant now = clock.instant();
                               final Instant expiresAt = now.plus(ttl);

                               return Session.issue(
                                               user.id(),
                                               now,
                                               expiresAt)
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
                                                                       final AccessTokenClaims claims = new AccessTokenClaims(
                                                                               user.id(),
                                                                               userAccessSnapshot.roles());
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
                                       );
                           })
                   );
       }
    }
}