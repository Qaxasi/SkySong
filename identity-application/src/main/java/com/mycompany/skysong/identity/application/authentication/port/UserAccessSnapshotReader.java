package com.mycompany.skysong.identity.application.authentication.port;

import com.mycompany.skysong.identity.application.authentication.model.UserAccessSnapshot;
import com.mycompany.skysong.identity.domain.UserId;
import com.mycompany.skysong.core.result.Result;

public interface UserAccessSnapshotReader {
    Result<UserAccessSnapshot> load(UserId userId);
}
