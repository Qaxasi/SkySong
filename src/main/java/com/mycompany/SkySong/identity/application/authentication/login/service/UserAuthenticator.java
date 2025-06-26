package com.mycompany.SkySong.identity.application.authentication.login.service;

import com.mycompany.SkySong.identity.adapter.out.redis.exception.SessionStoreException;
import com.mycompany.SkySong.identity.application.authentication.login.dto.AuthenticatedUser;
import com.mycompany.SkySong.identity.application.authentication.shared.dto.AuthenticationTokens;
import com.mycompany.SkySong.identity.application.authentication.login.dto.LoginInput;
import com.mycompany.SkySong.identity.application.authentication.login.exception.InvalidCredentialsException;
import com.mycompany.SkySong.identity.application.authentication.login.ports.Authenticator;
import com.mycompany.SkySong.identity.application.authentication.shared.dto.SessionData;
import com.mycompany.SkySong.identity.application.authentication.shared.ports.AccessTokenGenerator;
import com.mycompany.SkySong.identity.application.authentication.shared.ports.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.application.authentication.shared.ports.SessionStore;
import com.mycompany.SkySong.identity.domain.AccessToken;
import com.mycompany.SkySong.identity.domain.RefreshToken;
import com.mycompany.SkySong.shared.config.security.jwt.RefreshTokenProperties;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class UserAuthenticator {
    private final Authenticator authenticator;
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final SessionStore sessionStore;
    private final ApplicationLogger logger;
    private final Duration expiresAfter;
    private final Clock clock;

    public UserAuthenticator(final Authenticator authenticator,
                             final AccessTokenGenerator accessTokenGenerator,
                             final RefreshTokenGenerator refreshTokenGenerator,
                             final SessionStore sessionStore,
                             final ApplicationLogger logger,
                             final RefreshTokenProperties refreshTokenProperties,
                             final Clock clock) {
        this.authenticator = authenticator;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.sessionStore = sessionStore;
        this.logger = logger;
        this.expiresAfter = refreshTokenProperties.getDuration();
        this.clock = clock;
    }

    public Result<AuthenticationTokens> login(final LoginInput loginInput) {
        final AuthenticatedUser user;

        try {
            user = authenticator.authenticate(
                    loginInput.username(),
                    loginInput.password());
        } catch (InvalidCredentialsException ex) {
            logger.warn("Failed login attempt", context("username", loginInput.username()));
            return Result.failure(ex.getMessage(), ErrorType.INVALID_LOGIN_CREDENTIALS);
        }

        final AccessToken accessToken = accessTokenGenerator.generate(user);
        final RefreshToken refreshToken = refreshTokenGenerator.generate();

        final Result<Void> result = saveSession(refreshToken, user);
        if (result.isFailure()) {
            return Result.failure(result.errorMessage(), result.errorType());
        }

        logger.info("User logged in successfully", context("userId", user.id()));

        return Result.success(new AuthenticationTokens(accessToken, refreshToken));
    }

    private Result<Void> saveSession(final RefreshToken refreshToken, final AuthenticatedUser user) {
        final Instant now = Instant.now(clock);
        final Instant refreshTokenExpiresAt = now.plus(expiresAfter);

        try {
            sessionStore.save(refreshToken, new SessionData(
                    user.id(), user.username(), user.roles(), now, refreshTokenExpiresAt));
            return Result.success();
        } catch (SessionStoreException ex) {
            logger.error("Failed to save session", ex, context(Map.of(
                    "userId", user.id(),
                    "username", user.username())));
            return Result.failure("Unable to store session", ErrorType.SESSION_STORE_FAILURE);
        }
    }
}
