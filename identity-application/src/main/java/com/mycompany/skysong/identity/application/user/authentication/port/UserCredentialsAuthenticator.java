package com.mycompany.skysong.identity.application.user.authentication.port;

import com.mycompany.skysong.identity.application.user.authentication.model.AuthenticatedPrincipal;
import com.mycompany.skysong.identity.domain.RawPassword;
import com.mycompany.skysong.identity.domain.Username;
import com.mycompany.skysong.core.result.Result;

public interface UserCredentialsAuthenticator {
    Result<AuthenticatedPrincipal> authenticate(Username username, RawPassword password);
}
