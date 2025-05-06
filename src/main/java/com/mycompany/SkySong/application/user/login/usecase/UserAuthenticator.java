package com.mycompany.SkySong.application.user.login.usecase;

import com.mycompany.SkySong.application.user.login.dto.LoginInput;
import com.mycompany.SkySong.adapter.user.login.exception.InvalidCredentialsException;
import com.mycompany.SkySong.application.user.login.dto.AuthenticatedUser;
import com.mycompany.SkySong.application.user.login.dto.AuthenticationTokens;
import com.mycompany.SkySong.application.user.login.port.Authenticator;
import com.mycompany.SkySong.application.user.login.port.LoginTokenGenerator;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class UserAuthenticator {
    private final Authenticator authenticator;
    private final LoginTokenGenerator tokenGenerator;
    private final ApplicationLogger logger;

    public UserAuthenticator(final Authenticator authenticator,
                             final LoginTokenGenerator tokenGenerator,
                             final ApplicationLogger logger) {
        this.authenticator = authenticator;
        this.tokenGenerator = tokenGenerator;
        this.logger = logger;
    }

    public Result<AuthenticationTokens> login(final LoginInput loginInput) {
        try {
            final AuthenticatedUser user = authenticator.authenticate(
                            loginInput.usernameOrEmail(),
                            loginInput.password());

            final AuthenticationTokens tokens = tokenGenerator.generate(user);

            logger.info("User logged in successfully", context("userId", user.id()));

            return Result.success(new AuthenticationTokens(tokens.accessToken(), tokens.refreshToken()));
        } catch (final InvalidCredentialsException ex) {
            return Result.failure(ex.getMessage(), ex.getErrorType());
        }
    }
}
