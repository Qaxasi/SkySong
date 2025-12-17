package com.mycompany.skysong.identity.application.user.deletion.port;

import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;

public interface UserDeleter {
    Result<Unit> deleteEverythingById(int id);
}
