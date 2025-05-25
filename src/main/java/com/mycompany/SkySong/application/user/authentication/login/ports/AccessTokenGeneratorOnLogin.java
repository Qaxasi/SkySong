package com.mycompany.SkySong.application.user.login.ports;

import com.mycompany.SkySong.application.user.login.dto.AccessToken;
import com.mycompany.SkySong.application.user.login.dto.AuthenticatedUser;

public interface AccessTokenGeneratorOnLogin {
    AccessToken generate(AuthenticatedUser user);
}
