package com.mycompany.SkySong.application.user.token.refresh.port;

import com.mycompany.SkySong.application.user.token.refresh.dto.SessionUser;

public interface SessionUserStore {
    void save(String refreshToken, SessionUser user);
    SessionUser getUserByRefreshToken(String refreshToken);
}
