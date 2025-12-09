package com.mycompany.skysong.identity.application.registration.port;

import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;
import com.mycompany.skysong.identity.domain.User;

public interface UserStore {
    Result<Unit> save(User user);
}
