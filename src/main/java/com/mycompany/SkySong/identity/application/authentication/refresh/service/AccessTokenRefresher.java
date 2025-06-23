package com.mycompany.SkySong.application.user.authentication.session.refresh.usecase;

import com.mycompany.SkySong.adapter.redis.session.exception.SessionStoreException;
import com.mycompany.SkySong.application.user.authentication.dto.AccessToken;
import com.mycompany.SkySong.application.user.authentication.dto.RefreshToken;
import com.mycompany.SkySong.application.user.authentication.dto.SessionData;
import com.mycompany.SkySong.application.user.authentication.dto.AuthenticationTokens;
import com.mycompany.SkySong.identity.application.authentication.ports.AccessTokenGenerator;
import com.mycompany.SkySong.identity.application.authentication.ports.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.application.authentication.ports.SessionStore;
import com.mycompany.SkySong.shared.config.security.jwt.RefreshTokenProperties;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class AccessTokenRefresher {
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final Duration refreshTokenTtl;
    private final SessionStore sessionStore;
    private final ApplicationLogger logger;
    private final Clock clock;

    public AccessTokenRefresher(final AccessTokenGenerator accessTokenGenerator,
                                final RefreshTokenGenerator refreshTokenGenerator,
                                final RefreshTokenProperties refreshTokenProperties,
                                final SessionStore sessionStore,
                                final ApplicationLogger logger,
                                final Clock clock) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.refreshTokenTtl = refreshTokenProperties.getDuration();
        this.sessionStore = sessionStore;
        this.logger = logger;
        this.clock = clock;
    }

    public Result<AuthenticationTokens> refreshAccessToken(final RefreshToken refreshToken) {
        final SessionData session;

        try {
            session = sessionStore.findByToken(refreshToken).orElse(null);
        } catch (SessionStoreException ex) {
            logger.error("Failed to retrieve user session during access token refresh", ex);
            return Result.failure("Failed to retrieve session", ErrorType.SESSION_STORE_FAILURE);
        }

        final Result<Void> validationResult = validateSession(session);
        if (validationResult.isFailure()) {
            return Result.failure(validationResult.errorMessage(), validationResult.errorType());
        }

        final AccessToken newAccessToken = accessTokenGenerator.generate(session);


        final RefreshToken newRefreshToken = refreshTokenGenerator.generate();
        final Instant now = Instant.now(clock);
        final Instant newExpiresAt = now.plus(refreshTokenTtl);

        try {
            sessionStore.delete(refreshToken);
            sessionStore.save(newRefreshToken, new SessionData(
                    session.id(), session.usernameOrEmail(), session.roles(), now, newExpiresAt));
        } catch (SessionStoreException ex) {
            logger.error("Failed to rotate refresh token",ex, context(Map.of(
                   "userId", session.id())));
            return Result.failure("Could not refresh session", ErrorType.SESSION_STORE_FAILURE);
        }


        return Result.success(new AuthenticationTokens(newAccessToken, newRefreshToken));

    }

    private Result<Void> validateSession(final SessionData session) {
        if (session == null) {
            logger.warn("User session not found");
            return Result.failure("Invalid refresh token", ErrorType.INVALID_REFRESH_TOKEN);
        }

        Instant now = Instant.now(clock);
        if (session.expiresAt().isBefore(now)) {
            logger.warn("Refresh token expired", context("userId", session.id()));
            return Result.failure("Refresh token expired", ErrorType.EXPIRED_REFRESH_TOKEN);
        }
        return Result.success();
    }
}