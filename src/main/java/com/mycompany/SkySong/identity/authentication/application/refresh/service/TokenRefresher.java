package com.mycompany.SkySong.identity.authentication.application.refresh.service;

import com.mycompany.SkySong.identity.authentication.application.refresh.port.RefreshTokenRotator;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessTokenClaims;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AuthenticationTokens;
import com.mycompany.SkySong.identity.authentication.application.shared.port.AccessTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.shared.port.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.shared.port.SessionStore;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessToken;
import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class TokenRefresher {
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final SessionStore sessionStore;
    private final RefreshTokenRotator refreshTokenRotator;
    private final ApplicationLogger logger;
    private final Clock clock;

    public TokenRefresher(final AccessTokenGenerator accessTokenGenerator,
                          final RefreshTokenGenerator refreshTokenGenerator,
                          final SessionStore sessionStore,
                          final RefreshTokenRotator refreshTokenRotator,
                          final ApplicationLogger logger,
                          final Clock clock) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.sessionStore = sessionStore;
        this.refreshTokenRotator = refreshTokenRotator;
        this.logger = logger;
        this.clock = clock;
    }

    public Result<AuthenticationTokens> refresh(final String rawRefreshToken) {
        return validate(rawRefreshToken)
                .flatMap(refreshToken -> {
                    final Instant now = clock.instant();
                    return fetchAndValidateSession(refreshToken, now)
                            .flatMap(session -> refreshTokenGenerator.generate()
                                    .flatMap(newRefreshToken -> rotateRefreshTokenInSession(refreshToken, session, newRefreshToken, now)
                                            .map(ignored -> {
                                                final AccessTokenClaims claims =
                                                        new AccessTokenClaims(session.userId(), session.username(), session.roles());
                                                final AccessToken newAccessToken = accessTokenGenerator.generate(claims);
                                                return new AuthenticationTokens(newAccessToken, newRefreshToken);
                                            })
                                    )
                            );
                });
    }

    private Result<String> validate(final String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return Result.failure("Refresh token is missing or invalid", ErrorType.INVALID_REFRESH_TOKEN);
        }
        return Result.success(refreshToken);
    }

    private Result<Session> fetchAndValidateSession(final String refreshToken, final Instant now) {
        return sessionStore.findByToken(refreshToken)
                .mapError((type, message) -> {
                    if (type == ErrorType.REFRESH_TOKEN_NOT_FOUND) {
                        return Result.failure("Refresh token is invalid", ErrorType.INVALID_REFRESH_TOKEN);
                    } else {
                        return Result.failure(message, type);
                    }
                })
                .flatMap(session -> {
                    if (session.isRefreshTokenExpired(now)) {
                        logger.info("Refresh token expired", context("userId", session.userId()));
                        return Result.failure("Refresh token is invalid", ErrorType.INVALID_REFRESH_TOKEN);
                    }
                    return Result.success(session);
                });
    }

    private Result<Void> rotateRefreshTokenInSession(final String oldRefreshToken,
                                                     final Session session,
                                                     final RefreshToken newRefreshToken,
                                                     final Instant now) {

        return Session.create(session.userId(), session.username(), session.roles(), now, newRefreshToken.expiresAt())
                .flatMap(updatedSession -> refreshTokenRotator.rotate(
                        oldRefreshToken, newRefreshToken.value(), updatedSession))
                .mapError((type, message) -> {
                    if (type == ErrorType.REFRESH_TOKEN_NOT_FOUND) {
                        return Result.failure("Refresh token is invalid", ErrorType.INVALID_REFRESH_TOKEN);
                    } else {
                        return Result.failure(message, type);
                    }
                })

                .onFailure(r ->  {
                    if (r.errorType() == ErrorType.INVALID_REFRESH_TOKEN) {
                        logger.warn("Refresh token rotation failed - old refresh token not found", context("userId", session.userId()));
                    } else {
                        logger.error("Failed to rotate refresh token", context(Map.of(
                                "userId", session.userId(),
                                "errorType", r.errorType(),
                                "message", r.errorMessage())));
                    }
                });
    }
}
