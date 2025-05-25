package com.mycompany.SkySong.application.user.token.refresh.usecase;

import com.mycompany.SkySong.application.user.token.refresh.dto.SessionData;
import com.mycompany.SkySong.application.user.token.refresh.exception.ExpiredRefreshTokenException;
import com.mycompany.SkySong.application.user.token.refresh.exception.InvalidRefreshTokenException;
import com.mycompany.SkySong.application.user.token.refresh.ports.AccessTokenGeneratorOnRefresh;
import com.mycompany.SkySong.application.user.token.refresh.ports.SessionUserStore;
import com.mycompany.SkySong.application.user.token.refresh.ports.RefreshTokenValidator;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.shared.result.Result;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class AccessTokenRefresher {

    private final AccessTokenGeneratorOnRefresh accessTokenGenerator;
    private final SessionUserStore sessionUserStore;
    private final RefreshTokenValidator validator;
    private final ApplicationLogger logger;

    public AccessTokenRefresher(final AccessTokenGeneratorOnRefresh accessTokenGenerator,
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

            final SessionData user = sessionUserStore.getUserByRefreshToken(refreshToken);
            if (user == null) {
                logger.warn("No user session data for refresh token");
                return Result.failure(
                        "User session has expired.",
                        ErrorType.INVALID_REFRESH_TOKEN);
            }

            final String newAccessToken = accessTokenGenerator.generateAccessToken(user);
            logger.info("Access token refreshed", context("userId", user.id()));

            return Result.success(newAccessToken);

        } catch (ExpiredRefreshTokenException e) {
            logger.warn("Could not refresh access token - refresh token expired");
            return Result.failure(e.getMessage(), ErrorType.EXPIRED_REFRESH_TOKEN);

        } catch (InvalidRefreshTokenException e) {
            logger.warn("Could not refresh access token - invalid refresh token");
            return Result.failure(e.getMessage(), ErrorType.INVALID_REFRESH_TOKEN);
        }
    }
}
