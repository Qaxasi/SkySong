package com.mycompany.SkySong.application.user.token.refresh.ports;

import com.mycompany.SkySong.application.user.token.refresh.dto.SessionUser;

public interface AccessTokenGenerator {
    String generateAccessToken(SessionUser user);
}
