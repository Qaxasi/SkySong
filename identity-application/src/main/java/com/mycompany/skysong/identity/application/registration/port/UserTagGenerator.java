package com.mycompany.SkySong.identity.registration.application.port;

import com.mycompany.SkySong.identity.a.domain.UserTag;
import com.mycompany.SkySong.shared.result.Result;

public interface UserTagGenerator {
    Result<UserTag> generate();
}
