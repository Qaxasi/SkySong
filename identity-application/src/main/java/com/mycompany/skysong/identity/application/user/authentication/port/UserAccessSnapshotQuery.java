package com.mycompany.skysong.identity.application.user.authentication.port;

import com.mycompany.skysong.identity.application.user.authentication.model.UserAccessSnapshot;
import com.mycompany.skysong.identity.domain.UserId;
import com.mycompany.skysong.core.result.Result;

public interface UserAccessSnapshotQuery {
    Result<UserAccessSnapshot> fetch(UserId userId);
}
