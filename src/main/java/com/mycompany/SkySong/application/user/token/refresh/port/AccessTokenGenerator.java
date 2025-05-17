package com.mycompany.SkySong.application.user.token.refresh.port;

import com.mycompany.SkySong.application.user.token.refresh.dto.SessionUser;

public interface AccessTokenGenerator {
    String generateAccessToken(SessionUser user);
}
