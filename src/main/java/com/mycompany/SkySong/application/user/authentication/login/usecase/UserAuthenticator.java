package com.mycompany.SkySong.application.user.authentication.login.usecase;

import com.mycompany.SkySong.adapter.redis.session.exception.SessionStoreException;
import com.mycompany.SkySong.application.user.authentication.dto.AccessToken;
import com.mycompany.SkySong.application.user.authentication.dto.RefreshToken;
import com.mycompany.SkySong.application.user.authentication.login.dto.AuthenticatedUser;
import com.mycompany.SkySong.application.user.authentication.dto.AuthenticationTokens;
import com.mycompany.SkySong.application.user.authentication.login.dto.LoginInput;
import com.mycompany.SkySong.application.user.authentication.login.exception.InvalidCredentialsException;
import com.mycompany.SkySong.application.user.authentication.login.ports.Authenticator;
import com.mycompany.SkySong.application.user.authentication.dto.SessionData;
import com.mycompany.SkySong.application.user.authentication.ports.AccessTokenGenerator;
import com.mycompany.SkySong.application.user.authentication.ports.RefreshTokenGenerator;
import com.mycompany.SkySong.application.user.authentication.ports.SessionStore;
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
        try {
            final AuthenticatedUser user = authenticator.authenticate(
                            loginInput.username(),
                            loginInput.password());

            final AccessToken accessToken = accessTokenGenerator.generate(user);
            final RefreshToken refreshToken = refreshTokenGenerator.generate();

            final Result<Void> saveSessionResult = saveSession(refreshToken, user);
            if (saveSessionResult.isFailure()) {
                return Result.failure(saveSessionResult.errorMessage(), saveSessionResult.errorType());
            }

            logger.info("User logged in successfully", context(Map.of(
                    "userId", user.id(),
                    "username", user.username(),
                    "roles", user.roles()
            )));

            return Result.success(new AuthenticationTokens(accessToken, refreshToken));
        } catch (InvalidCredentialsException ex) {
            logger.warn("Failed login attempt", context("username", loginInput.username()));
            return Result.failure(ex.getMessage(), ErrorType.INVALID_LOGIN_CREDENTIALS);
        }
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
