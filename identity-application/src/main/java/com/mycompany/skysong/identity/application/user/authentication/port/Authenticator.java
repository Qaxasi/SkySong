package com.mycompany.skysong.identity.application.user.authentication.port;

import com.mycompany.skysong.identity.application.user.authentication.model.AuthenticatedIdentity;
import com.mycompany.skysong.identity.domain.RawPassword;
import com.mycompany.skysong.identity.domain.Username;
import com.mycompany.skysong.core.result.Result;

public interface Authenticator {
    Result<AuthenticatedIdentity> authenticate(Username username, RawPassword password);
}
