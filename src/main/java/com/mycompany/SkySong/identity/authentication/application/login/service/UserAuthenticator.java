package com.mycompany.SkySong.identity.authentication.application.login.service;

import com.mycompany.SkySong.identity.authentication.application.shared.dto.AuthenticationTokens;
import com.mycompany.SkySong.identity.authentication.application.login.dto.LoginCredentials;
import com.mycompany.SkySong.identity.authentication.application.login.port.Authenticator;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessTokenClaims;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.authentication.application.shared.port.AccessTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.shared.port.RefreshTokenGenerator;
import com.mycompany.SkySong.identity.authentication.application.shared.port.SessionStore;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessToken;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;

import java.time.Clock;
import java.time.Instant;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class UserAuthenticator {
    private final Authenticator authenticator;
    private final AccessTokenGenerator accessTokenGenerator;
    private final RefreshTokenGenerator refreshTokenGenerator;
    private final SessionStore sessionStore;
    private final ApplicationLogger logger;
    private final Clock clock;

    public UserAuthenticator(final Authenticator authenticator,
                             final AccessTokenGenerator accessTokenGenerator,
                             final RefreshTokenGenerator refreshTokenGenerator,
                             final SessionStore sessionStore,
                             final ApplicationLogger logger,
                             final Clock clock) {
        this.authenticator = authenticator;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
        this.sessionStore = sessionStore;
        this.logger = logger;
        this.clock = clock;
    }


    public Result<AuthenticationTokens> login(final LoginCredentials rawCredentials) {
        return validateAndNormalize(rawCredentials)
                .flatMap(credentials ->
                        authenticator.authenticate(credentials.username(), credentials.password())
                                .onFailure(err -> logAuthFailure(err.errorType()))
                )
                .flatMap(user -> refreshTokenGenerator.generate()
                        .flatMap(refreshToken -> {
                            final Instant now = clock.instant();

                            return Session.create(user.id(), user.username(), user.roles(), now, refreshToken.expiresAt())
                                    .flatMap(session -> sessionStore.save(refreshToken.value(), session)
                                            .map(ignored -> {
                                                final AccessTokenClaims claims =
                                                        new AccessTokenClaims(user.id(), user.username(), user.roles());
                                                final AccessToken accessToken = accessTokenGenerator.generate(claims);

                                                logger.info("User logged in successfully", context("userId", user.id()));
                                                return new AuthenticationTokens(accessToken, refreshToken);
                                            })
                                    );
                        })
                );
    }
    private void logAuthFailure(final ErrorType errorType) {
        switch (errorType) {
            case AUTHENTICATION_FAILED -> logger.warn("Invalid login attempt");
            case ACCOUNT_RESTRICTED -> logger.warn("Login blocked(account restricted)");
            default -> logger.error("Login failed", context("errorType", errorType));
        }
    }

    private Result<LoginCredentials> validateAndNormalize(final LoginCredentials credentials) {
        if (credentials == null) {
            return Result.failure("Missing login payload", ErrorType.VALIDATION_ERROR);
        }

        final String rawUsername = credentials.username();
        if (rawUsername == null) {
            return Result.failure("Username must not be blank", ErrorType.VALIDATION_ERROR);
        }

        final String username = rawUsername.strip();

        final String password = credentials.password();

        if (username.isEmpty() || password == null || password.isBlank()) {
            return Result.failure("Username and password must not be blank", ErrorType.VALIDATION_ERROR);
        }
      return Result.success(new LoginCredentials(username, password));
    }
}
