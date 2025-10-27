package com.mycompany.SkySong.identity.authentication.application.login.port;

import com.mycompany.SkySong.identity.authentication.application.login.dto.AuthenticatedIdentity;
import com.mycompany.SkySong.identity.authentication.domain.RawPassword;
import com.mycompany.SkySong.identity.authentication.domain.Username;
import com.mycompany.SkySong.shared.result.Result;

public interface Authenticator {
    Result<AuthenticatedIdentity> authenticate(Username username, RawPassword password);
}
