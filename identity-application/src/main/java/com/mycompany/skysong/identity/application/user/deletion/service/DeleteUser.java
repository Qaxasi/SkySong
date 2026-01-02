package com.mycompany.skysong.identity.application.user.deletion.service;

import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;
import com.mycompany.skysong.identity.application.user.deletion.port.UserDeleter;
import com.mycompany.skysong.identity.domain.UserId;

public class DeleteUser {
    private final UserDeleter userDeleter;
    public DeleteUser(final UserDeleter userDeleter) {
        this.userDeleter = userDeleter;
    }

    public Result<Unit> delete(final UserId userId) {
        return userDeleter.delete(userId);
    }
}