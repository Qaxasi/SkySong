package com.mycompany.skysong.identity.application.user.registration.port;

import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;
import com.mycompany.skysong.identity.domain.User;

public interface UserSaver {
    Result<Unit> save(User user);
}
