package com.mycompany.skysong.identity.application.registration.port;

import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.identity.application.registration.model.UniquenessStatus;
import com.mycompany.skysong.identity.domain.Email;
import com.mycompany.skysong.identity.domain.Username;

public interface UserUniquenessChecker {
    Result<UniquenessStatus> check(Username username, Email email);
}
