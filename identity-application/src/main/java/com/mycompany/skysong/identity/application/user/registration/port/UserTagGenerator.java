package com.mycompany.skysong.identity.application.user.registration.port;

import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.identity.domain.UserTag;

public interface UserTagGenerator {
    Result<UserTag> generate();
}
