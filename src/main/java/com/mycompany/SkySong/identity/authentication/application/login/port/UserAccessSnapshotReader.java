package com.mycompany.SkySong.identity.authentication.application.login.port;

import com.mycompany.SkySong.identity.authentication.application.login.dto.UserAccessSnapshot;
import com.mycompany.SkySong.identity.shared.domain.UserId;
import com.mycompany.SkySong.shared.result.Result;

public interface UserAccessSnapshotReader {
    Result<UserAccessSnapshot> load(UserId userId);
}
