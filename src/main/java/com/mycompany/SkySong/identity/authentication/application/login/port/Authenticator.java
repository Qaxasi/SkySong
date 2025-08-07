package com.mycompany.SkySong.identity.authentication.login.application.port;

import com.mycompany.SkySong.identity.authentication.login.application.dto.AuthenticatedUser;

public interface Authenticator {
    AuthenticatedUser authenticate(String username, String password);
}
