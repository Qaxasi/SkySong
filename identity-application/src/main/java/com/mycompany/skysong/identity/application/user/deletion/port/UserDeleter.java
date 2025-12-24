package com.mycompany.skysong.identity.application.user.deletion.port;

import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;
import com.mycompany.skysong.identity.domain.UserId;

public interface UserDeleter {
    Result<Unit> deleteById(UserId id);
}
