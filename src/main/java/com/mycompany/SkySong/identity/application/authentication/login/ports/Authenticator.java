package com.mycompany.SkySong.identity.application.authentication.login.ports;

import com.mycompany.SkySong.identity.application.authentication.login.dto.AuthenticatedUser;

public interface Authenticator {
    AuthenticatedUser authenticate(String username, String password);
}
