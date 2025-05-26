package com.mycompany.SkySong.application.user.authentication.login.usecase;

import com.mycompany.SkySong.application.user.authentication.dto.AccessToken;
import com.mycompany.SkySong.application.user.authentication.dto.RefreshToken;
import com.mycompany.SkySong.application.user.authentication.login.dto.AuthenticatedUser;
import com.mycompany.SkySong.application.user.authentication.login.dto.AuthenticationTokens;
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
                            loginInput.usernameOrEmail(),
                            loginInput.password());

            final AccessToken accessToken = accessTokenGenerator.generate(user);
            final RefreshToken refreshToken = refreshTokenGenerator.generate();

            final Instant now = Instant.now(clock);
            final Instant refreshTokenExpiresAt = now.plus(expiresAfter);

            sessionStore.save(refreshToken, new SessionData(
                    user.id(), user.usernameOrEmail(), user.roles(), now, refreshTokenExpiresAt));

            logger.info("User logged in successfully", context(Map.of(
                    "userId", user.id(),
                    "usernameOrEmail", user.usernameOrEmail(),
                    "roles", user.roles()
            )));

            return Result.success(new AuthenticationTokens(accessToken, refreshToken));
        } catch (final InvalidCredentialsException ex) {
            logger.warn("Failed login attempt", context("usernameOrEmail", loginInput.usernameOrEmail()));
            return Result.failure(ex.getMessage(), ErrorType.INVALID_LOGIN_CREDENTIALS);
        }
    }
}
