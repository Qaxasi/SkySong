package com.mycompany.SkySong.application.user.delete.usecase;

import com.mycompany.SkySong.adapter.user.deletion.persistence.UserNotFoundException;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.application.user.delete.ports.UserDeletion;
import com.mycompany.SkySong.shared.result.Result;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class UserDeletionUseCase {
    private final UserDeletion userDeletion;
    private final ApplicationLogger logger;

    public UserDeletionUseCase(final UserDeletion userDeletion,
                               final ApplicationLogger logger) {
        this.userDeletion = userDeletion;
        this.logger = logger;
    }

    public Result<Void> delete(final Integer userId) {
        if (userId == null) {
            logger.warn("User deletion failed - missing user id");
            return Result.failure("User id is required and was not provided", ErrorType.MISSING_USER_ID);
        }

        try {
            userDeletion.deleteEverythingById(userId);
        } catch (UserNotFoundException ex) {
            logger.warn("Attempted to delete non-existent user", context("userId", userId));
            return Result.failure(ex.getMessage(), ErrorType.USER_NOT_FOUND);
        }

        logger.info("User deleted successfully", context("userId", userId));
        return Result.success();
    }
}