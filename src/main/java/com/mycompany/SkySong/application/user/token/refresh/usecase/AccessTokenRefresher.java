package com.mycompany.SkySong.application.user.token.refresh.usecase;

import com.mycompany.SkySong.application.user.token.refresh.dto.SessionUser;
import com.mycompany.SkySong.application.user.token.refresh.exception.RefreshTokenValidationException;
import com.mycompany.SkySong.application.user.token.refresh.port.AccessTokenGenerator;
import com.mycompany.SkySong.application.user.token.refresh.port.SessionUserStore;
import com.mycompany.SkySong.application.user.token.refresh.port.RefreshTokenValidator;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class AccessTokenRefresher {

    private final AccessTokenGenerator accessTokenGenerator;
    private final SessionUserStore sessionUserStore;
    private final RefreshTokenValidator validator;
    private final ApplicationLogger logger;

    public AccessTokenRefresher(final AccessTokenGenerator accessTokenGenerator,
                                final SessionUserStore sessionUserStore,
                                final RefreshTokenValidator validator,
                                final ApplicationLogger logger) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.sessionUserStore = sessionUserStore;
        this.validator = validator;
        this.logger = logger;
    }

    public Result<String> refreshAccessToken(final String refreshToken) {
        try {
            validator.validateToken(refreshToken);

            final SessionUser user = sessionUserStore.getUserByRefreshToken(refreshToken);
            if (user == null) {
                logger.warn("No user session data for refresh token");
                return Result.failure(
                        "User session has expired.",
                        ErrorType.INVALID_REFRESH_TOKEN);
            }

            final String newAccessToken = accessTokenGenerator.generateAccessToken(user);
            logger.info("Access token refreshed", context("userId", user.id()));

            return Result.success(newAccessToken);
        } catch (RefreshTokenValidationException e) {
            logger.warn("Refresh token validation failed", context("error", e.getErrorType().name()));
            return Result.failure(e.getMessage(), e.getErrorType());
        }
    }
}
