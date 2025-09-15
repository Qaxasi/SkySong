package com.mycompany.SkySong.identity.session.application.port;

import com.mycompany.SkySong.identity.shared.domain.UserKey;
import com.mycompany.SkySong.shared.result.Result;

public interface SessionVersionStore {
    Result<Long> current(UserKey userKey);
    Result<Long> increment(UserKey userKey);
}
