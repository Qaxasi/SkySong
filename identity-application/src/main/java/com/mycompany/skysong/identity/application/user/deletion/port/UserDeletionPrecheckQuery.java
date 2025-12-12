package com.mycompany.skysong.identity.application.user.deletion.port;

import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.identity.application.user.deletion.model.UserDeletionPrecheck;
import com.mycompany.skysong.identity.domain.UserId;

public interface UserDeletionPrecheckQuery {
    Result<UserDeletionPrecheck> fetch(UserId userId);
}
