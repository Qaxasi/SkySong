package com.mycompany.SkySong.identity.authentication.application.shared.port;

import com.mycompany.SkySong.identity.shared.domain.UserTag;
import com.mycompany.SkySong.shared.result.Result;

public interface UserKeyStore {
    Result<UserTag> getByUserId(int userId);
}
