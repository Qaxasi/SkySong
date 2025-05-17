package com.mycompany.SkySong.application.user.login.ports;

import com.mycompany.SkySong.application.user.login.dto.AuthenticatedUser;
import com.mycompany.SkySong.application.user.login.dto.AuthenticationTokens;

public interface LoginTokenGenerator {
    AuthenticationTokens generate(AuthenticatedUser user);
}
