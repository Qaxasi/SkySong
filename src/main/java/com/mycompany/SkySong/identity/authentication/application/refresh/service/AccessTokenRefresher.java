package com.mycompany.SkySong.identity.application.authentication.refresh.service;

import com.mycompany.SkySong.identity.adapter.out.redis.exception.SessionStoreException;
import com.mycompany.SkySong.identity.application.authentication.refresh.ports.RefreshTokenRotator;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.SessionData;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AuthenticationTokens;
import com.mycompany.SkySong.identity.authentication.application.shared.ports.AccessTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.shared.ports.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.shared.ports.SessionStore;
import com.mycompany.SkySong.identity.domain.AccessToken;
import com.mycompany.SkySong.identity.domain.RefreshToken;
import com.mycompany.SkySong.infrastructure.security.refreshToken.RefreshTokenProperties;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class AccessTokenRefresher {
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final SessionStore sessionStore;
    private final RefreshTokenRotator refreshTokenRotator;
    private final Duration refreshTokenTtl;
    private final ApplicationLogger logger;
    private final Clock clock;

    public AccessTokenRefresher(final AccessTokenGenerator accessTokenGenerator,
                                final RefreshTokenGenerator refreshTokenGenerator,
                                final RefreshTokenProperties refreshTokenProperties,
                                final SessionStore sessionStore,
                                final RefreshTokenRotator refreshTokenRotator,
                                final ApplicationLogger logger,
                                final Clock clock) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.refreshTokenTtl = refreshTokenProperties.duration();
        this.sessionStore = sessionStore;
        this.refreshTokenRotator = refreshTokenRotator;
        this.logger = logger;
        this.clock = clock;
    }

    public Result<AuthenticationTokens> refreshAccessToken(final RefreshToken refreshToken) {
        return validateInput(refreshToken)
                .flatMap(ignored -> fetchAndValidateSession(refreshToken))
                .flatMap(session -> {
                    final AccessToken newAccessToken = accessTokenGenerator.generate(session);
                    final RefreshToken newRefreshToken = refreshTokenGenerator.generate();

                    return rotateRefreshTokenInSession(refreshToken, session, newRefreshToken)
                            .map(ignored -> new AuthenticationTokens(newAccessToken, newRefreshToken));
                });
    }

    private Result<Void> validateInput(final RefreshToken refreshToken) {
        if (refreshToken == null || refreshToken.value() == null || refreshToken.value().isBlank()) {
            return Result.failure("Refresh token is missing", ErrorType.INVALID_REFRESH_TOKEN);
        }
        return Result.success();
    }

    private Result<SessionData> fetchAndValidateSession(final RefreshToken refreshToken) {
        final SessionData session;
        try {
            session = sessionStore.findByToken(refreshToken).orElse(null);
        } catch (SessionStoreException ex) {
            logger.error("Failed to retrieve user session during access token refresh", ex);
            return Result.failure("Failed to retrieve session", ErrorType.SESSION_STORE_FAILURE);
        }
        if (session == null) {
            logger.warn("User session not found");
            return Result.failure("Invalid refresh token", ErrorType.INVALID_REFRESH_TOKEN);
        }

        final Instant now = Instant.now(clock);
        if (session.expiresAt().isBefore(now)) {
            logger.warn("Refresh token expired", context("userId", session.userId()));
            return Result.failure("Refresh token expired", ErrorType.EXPIRED_REFRESH_TOKEN);
        }
        return Result.success(session);
    }

    private Result<Void> rotateRefreshTokenInSession(final RefreshToken oldRefreshToken,
                                                     final SessionData sessionData,
                                                     final RefreshToken newRefreshToken) {
        final Instant now = Instant.now(clock);
        final Instant expiresAt = now.plus(refreshTokenTtl);

        final SessionData updatedSessionData = new SessionData(
                sessionData.userId(), sessionData.username(), sessionData.roles(), now, expiresAt);

        try {
            refreshTokenRotator.rotate(oldRefreshToken, newRefreshToken, updatedSessionData);
            return Result.success();
        } catch (SessionStoreException ex) {
            logger.error("Failed to rotate refresh token",ex, context("userId", sessionData.userId()));
            return Result.failure("Could not refresh session", ErrorType.SESSION_STORE_FAILURE);
        }
    }
}