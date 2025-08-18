package com.mycompany.SkySong.identity.authentication.application.login.port;

import com.mycompany.SkySong.identity.authentication.application.login.dto.AuthenticatedUser;
import com.mycompany.SkySong.shared.result.Result;

public interface Authenticator {
    Result<AuthenticatedUser> authenticate(String username, String password);
}
