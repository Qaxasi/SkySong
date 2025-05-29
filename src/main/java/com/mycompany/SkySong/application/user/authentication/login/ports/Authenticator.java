package com.mycompany.SkySong.application.user.authentication.login.ports;

import com.mycompany.SkySong.application.user.authentication.login.dto.AuthenticatedUser;

public interface Authenticator {
    AuthenticatedUser authenticate(String username, String password);
}
