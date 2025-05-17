package com.mycompany.SkySong.application.user.login.usecase;

import com.mycompany.SkySong.application.user.login.dto.LoginInput;
import com.mycompany.SkySong.application.user.login.exception.InvalidCredentialsException;
import com.mycompany.SkySong.application.user.login.dto.AuthenticatedUser;
import com.mycompany.SkySong.application.user.login.dto.AuthenticationTokens;
import com.mycompany.SkySong.application.user.login.ports.Authenticator;
import com.mycompany.SkySong.application.user.login.ports.LoginTokenGenerator;
import com.mycompany.SkySong.application.user.token.refresh.dto.SessionUser;
import com.mycompany.SkySong.application.user.token.refresh.ports.SessionUserStore;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class UserAuthenticator {
    private final Authenticator authenticator;
    private final LoginTokenGenerator tokenGenerator;
    private final SessionUserStore sessionUserStore;
    private final ApplicationLogger logger;

    public UserAuthenticator(final Authenticator authenticator,
                             final LoginTokenGenerator tokenGenerator,
                             final SessionUserStore sessionUserStore,
                             final ApplicationLogger logger) {
        this.authenticator = authenticator;
        this.tokenGenerator = tokenGenerator;
        this.sessionUserStore = sessionUserStore;
        this.logger = logger;
    }

    public Result<AuthenticationTokens> login(final LoginInput loginInput) {
        try {
            final AuthenticatedUser user = authenticator.authenticate(
                            loginInput.usernameOrEmail(),
                            loginInput.password());

            final AuthenticationTokens tokens = tokenGenerator.generate(user);

            sessionUserStore.save(tokens.refreshToken(), new SessionUser(user.id(), user.usernameOrEmail(), user.roles()));

            logger.info("User logged in successfully", context("userId", user.id()));

            return Result.success(new AuthenticationTokens(tokens.accessToken(), tokens.refreshToken()));
        } catch (final InvalidCredentialsException ex) {
            return Result.failure(ex.getMessage(), ErrorType.INVALID_LOGIN_CREDENTIALS);
        }
    }
}
