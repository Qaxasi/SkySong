package com.mycompany.SkySong.identity.authentication.application.shared.port;

import com.mycompany.SkySong.identity.shared.domain.UserTag;
import com.mycompany.SkySong.shared.result.Result;

public interface UserTagStore {
    Result<UserTag> getByUserId(int userId);
}
