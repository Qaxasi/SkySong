package com.mycompany.skysong.identity.application.user.deletion.service;

import com.mycompany.skysong.core.error.ErrorType;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;
import com.mycompany.skysong.identity.application.user.deletion.port.UserDeleter;
import com.mycompany.skysong.identity.application.user.deletion.port.UserDeletionPrecheckReader;
import com.mycompany.skysong.identity.domain.UserId;

public class DeleteUser {
    private final UserDeletionPrecheckReader deletionPrecheckReader;
    private final UserDeleter userDeleter;
    public DeleteUser(final UserDeletionPrecheckReader deletionPrecheckReader,
                      final UserDeleter userDeleter) {
        this.deletionPrecheckReader = deletionPrecheckReader;
        this.userDeleter = userDeleter;
    }

    public Result<Unit> delete(final UserId userId) {
        return deletionPrecheckReader.read(userId)
                .flatMap(precheck -> {
                    if (precheck.isLastAdmin()) {
                        return Result.failure("Cannot delete last admin", ErrorType.LAST_ADMIN_DELETE_FORBIDDEN);
                    }
                    return userDeleter.deleteById(userId);
                });
    }
}