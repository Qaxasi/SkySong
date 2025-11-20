package com.mycompany.SkySong.identity.authentication.application.service;

import com.mycompany.SkySong.identity.authentication.application.port.UserAccessSnapshotReader;
import com.mycompany.SkySong.identity.authentication.application.port.Authenticator;
import com.mycompany.SkySong.identity.authentication.application.dto.AccessToken;
import com.mycompany.SkySong.identity.authentication.application.dto.AccessTokenClaims;
import com.mycompany.SkySong.identity.authentication.application.dto.AccessGrant;
import com.mycompany.SkySong.identity.authentication.domain.RawPassword;
import com.mycompany.SkySong.identity.authentication.application.port.AccessTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.port.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.port.SessionStore;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.authentication.domain.Username;
import com.mycompany.SkySong.identity.infrastructure.authentication.config.token.SessionProperties;
import com.mycompany.SkySong.shared.result.Result;

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
                             final SessionProperties properties) {
        this.authenticator = authenticator;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.sessionStore = sessionStore;
        this.snapshotReader = snapshotReader;
        this.clock = clock;
        this.ttl = properties.ttl();
    }


    public Result<AccessGrant> login(final Username username, final RawPassword password) {
       try (password) {
           return authenticator.authenticate(username, password)
                   .flatMap(user -> snapshotReader.load(user.id())
                           .flatMap(snap -> {
                               final Instant now = clock.instant();
                               final Instant expiresAt = now.plus(ttl);

                               return Session.issue(
                                               user.id(),
                                               now,
                                               expiresAt,
                                               snap.accessVersion())
                                       .flatMap(session ->
                                               refreshTokenGenerator.generate()
                                                       .flatMap(rt -> {
                                                           final Duration sessionTtl = session.ttl(now);
                                                           return sessionStore.saveSession(
                                                                           snap.userTag(),
                                                                           rt,
                                                                           session,
                                                                           sessionTtl)
                                                                   .map(ignored -> {
                                                                       final AccessTokenClaims claims = new AccessTokenClaims(
                                                                               user.id(),
                                                                               snap.roles(),
                                                                               snap.accessVersion());
                                                                       final AccessToken at = accessTokenGenerator.generate(claims);

                                                                       final long expiresIn = at.expiresInSeconds(now);
                                                                       return new AccessGrant(
                                                                               at,
                                                                               expiresIn,
                                                                               rt,
                                                                               sessionTtl,
                                                                               snap.userTag());
                                                                   });
                                                       })
                                       );
                           })
                   );
       }
    }
}