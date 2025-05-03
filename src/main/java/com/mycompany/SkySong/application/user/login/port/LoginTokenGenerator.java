package com.mycompany.SkySong.application.user.login.port;

import com.mycompany.SkySong.application.user.login.model.AuthenticatedUser;
import com.mycompany.SkySong.application.user.login.model.AuthenticationTokens;

public interface LoginTokenGenerator {
    AuthenticationTokens generate(AuthenticatedUser user);
}
