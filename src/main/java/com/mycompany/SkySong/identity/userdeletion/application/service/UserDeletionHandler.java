package com.mycompany.SkySong.identity.userdeletion.application.service;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.identity.userdeletion.application.port.UserDeletion;
import com.mycompany.SkySong.shared.result.Result;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class UserDeletionHandler {
    private final UserDeletion userDeletion;
    private final ApplicationLogger logger;

    public UserDeletionHandler(final UserDeletion userDeletion,
                               final ApplicationLogger logger) {
        this.userDeletion = userDeletion;
        this.logger = logger;
    }

    public Result<Void> deleteUserById(final Integer userId) {
        return validateInput(userId)
                .flatMap(ignored -> userDeletion.deleteEverythingById(userId))
                .flatMap(ignored -> {
                    logger.info("User deleted successfully", context("userId", userId));
                    return Result.success();
                });
    }


    private Result<Void> validateInput(final Integer userId) {
        if (userId == null || userId <= 0) {
            logger.warn("User deletion failed - missing or invalid user ID", context("userId", userId));
            return Result.failure("User id is missing or invalid", ErrorType.MISSING_USER_ID);
        }
        return Result.success();
    }
}