package com.mycompany.SkySong.application.user.login.ports;

import com.mycompany.SkySong.application.user.login.dto.AuthenticatedUser;

public interface Authenticator {
    AuthenticatedUser authenticate(String usernameOrEmail, String password);
}
