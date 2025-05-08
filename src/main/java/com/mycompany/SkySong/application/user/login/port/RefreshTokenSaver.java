package com.mycompany.SkySong.application.user.login.port;

import com.mycompany.SkySong.application.user.login.dto.AuthenticatedUser;

public interface RefreshTokenSaver {
    void save(String refreshToken, AuthenticatedUser user);
}
