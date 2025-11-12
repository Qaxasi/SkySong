package com.mycompany.SkySong.identity.authentication.application.port;

import com.mycompany.SkySong.identity.authentication.application.dto.UserAccessSnapshot;
import com.mycompany.SkySong.identity.authentication.domain.UserId;
import com.mycompany.SkySong.shared.result.Result;

public interface UserAccessSnapshotReader {
    Result<UserAccessSnapshot> load(UserId userId);
}
