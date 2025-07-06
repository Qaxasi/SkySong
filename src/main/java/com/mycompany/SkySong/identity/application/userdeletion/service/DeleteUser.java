package com.mycompany.SkySong.identity.application.userdeletion.service;

import com.mycompany.SkySong.identity.adapter.out.db.exception.UserDeletionPersistenceException;
import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.shared.logging.ApplicationLogger;
import com.mycompany.SkySong.identity.application.userdeletion.ports.UserDeletion;
import com.mycompany.SkySong.shared.result.Result;

import static com.mycompany.SkySong.shared.logging.ApplicationLogger.Context.context;

public class DeleteUser {
    private final UserDeletion userDeletion;
    private final ApplicationLogger logger;

    public DeleteUser(final UserDeletion userDeletion,
                      final ApplicationLogger logger) {
        this.userDeletion = userDeletion;
        this.logger = logger;
    }

    public Result<Void> execute(final Integer userId) {
        return validateInput(userId)
                .flatMap(ignored -> deleteEverythingById(userId))
                .flatMap(ignored -> {
                    logger.info("User deleted successfully", context("userId", userId));
                    return Result.success();
                });
    }


    private Result<Void> validateInput(final Integer userId) {
        if (userId == null) {
            logger.warn("User deletion failed - missing user id");
            return Result.failure("User id is required and was not provided", ErrorType.MISSING_USER_ID);
        }
        return Result.success();
    }

    private Result<Void> deleteEverythingById(final int userId) {
        try {
            userDeletion.deleteEverythingById(userId);
        } catch (UserDeletionPersistenceException ex) {
            logger.error("Persistence error while deleting user", ex, context("userId", userId));
            return Result.failure("Failed to delete user due to persistence error", ErrorType.PERSISTENCE_ERROR);
        }
        return Result.success();
    }
}