package com.mycompany.SkySong.identity.authentication.application.login.service;

import com.mycompany.SkySong.identity.authentication.application.login.port.UserAccessSnapshotReader;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessToken;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessTokenClaims;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessGrant;
import com.mycompany.SkySong.identity.authentication.application.login.port.Authenticator;
import com.mycompany.SkySong.identity.authentication.domain.RawPassword;
import com.mycompany.SkySong.identity.authentication.application.shared.port.AccessTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.shared.port.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.shared.port.SessionStore;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.authentication.domain.Username;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.result.Result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static net.logstash.logback.argument.StructuredArguments.kv;

public class UserAuthenticator {
    private static final Logger log = LoggerFactory.getLogger(UserAuthenticator.class);
    private final Authenticator authenticator;
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final SessionStore sessionStore;
    private final UserAccessSnapshotReader snapshotReader;
    private final Clock clock;

    public UserAuthenticator(final Authenticator authenticator,
                             final AccessTokenGenerator accessTokenGenerator,
                             final RefreshTokenGenerator refreshTokenGenerator,
                             final SessionStore sessionStore,
                             final UserAccessSnapshotReader snapshotReader,
                             final Clock clock) {
        this.authenticator = authenticator;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.sessionStore = sessionStore;
        this.snapshotReader = snapshotReader;
        this.clock = clock;
    }


    public Result<AccessGrant> login(final Username username, final RawPassword password) {
       try (password) {
           return authenticator.authenticate(username, password)
                   .flatMap(user -> snapshotReader.load(user.id())
                           .flatMap(snap -> {
                               final Instant issuedAt = clock.instant();

                               return refreshTokenGenerator.generate(issuedAt)
                                       .flatMap(rt -> {
                                           final Duration rtTtl = rt.remainingTtl(issuedAt);
                                           if (rtTtl.isZero() || rtTtl.isNegative()) {
                                               log.error("refresh token TTL is non-positive {} {} {} {}",
                                                       kv("op", "user.login"),
                                                       kv("issuedAt", issuedAt),
                                                       kv("expiresAt", rt.expiresAt()),
                                                       kv("rtTtlSec", rtTtl.getSeconds()));
                                               return Result.failure("Internal error", ErrorType.INTERNAL_ERROR);
                                           }
                                           return Session.issue(user.id(), issuedAt, rtTtl, snap.accessVersion())
                                                   .flatMap(session ->
                                                       sessionStore.save(snap.userTag(), rt, session, rtTtl)
                                                               .map(ignored -> {
                                                                   final AccessTokenClaims claims =
                                                                           new AccessTokenClaims(user.id(), snap.roles(), snap.accessVersion());
                                                                   final AccessToken at = accessTokenGenerator.generate(claims);

                                                                   final Duration atTtl = at.remainingTtl(issuedAt);
                                                                   if (atTtl.isZero() || atTtl.isNegative()) {
                                                                       log.error("access token TTL is non positive {} {} {} {}",
                                                                               kv("op", "user.login"),
                                                                               kv("issuedAt", issuedAt),
                                                                               kv("expiresAt", at.expiresAt()),
                                                                               kv("atTtlSec", atTtl.getSeconds()));
                                                                   }
                                                                   return new AccessGrant(at, atTtl, rt, rtTtl);
                                                               })
                                                   );
                                       });
                           })
                   );
       }
    }
}