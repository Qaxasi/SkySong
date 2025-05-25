package com.mycompany.SkySong.application.user.authentication.session.refresh.ports;

import com.mycompany.SkySong.application.user.authentication.dto.SessionData;

public interface SessionUserStore {
    void save(String refreshToken, SessionData user);
    SessionData getUserByRefreshToken(String refreshToken);
}
