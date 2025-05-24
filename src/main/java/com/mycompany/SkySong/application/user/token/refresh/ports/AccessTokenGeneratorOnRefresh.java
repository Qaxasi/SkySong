package com.mycompany.SkySong.application.user.token.refresh.ports;

import com.mycompany.SkySong.application.user.login.dto.AccessToken;
import com.mycompany.SkySong.application.user.token.refresh.dto.SessionData;

public interface AccessTokenGeneratorOnRefresh {
    AccessToken generate(SessionData user);
}
