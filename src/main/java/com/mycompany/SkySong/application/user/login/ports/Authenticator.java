package com.mycompany.SkySong.application.user.login.port;

import com.mycompany.SkySong.application.user.login.dto.AuthenticatedUser;

public interface Authenticator {
    AuthenticatedUser authenticate(String usernameOrEmail, String password);
}
