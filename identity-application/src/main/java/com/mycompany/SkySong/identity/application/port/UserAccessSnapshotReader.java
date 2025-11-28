package com.mycompany.SkySong.identity.application.port;

import com.mycompany.SkySong.identity.application.dto.UserAccessSnapshot;
import com.mycompany.skysong.identity.domain.UserId;
import com.mycompany.skysong.core.result.Result;

public interface UserAccessSnapshotReader {
    Result<UserAccessSnapshot> load(UserId userId);
}
