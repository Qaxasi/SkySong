package com.mycompany.SkySong.application.user.authentication.ports;

import com.mycompany.SkySong.application.user.authentication.dto.RefreshToken;
import com.mycompany.SkySong.application.user.authentication.dto.SessionData;

import java.util.Optional;

public interface SessionStore {
    void save(RefreshToken token, SessionData sessionData);
    Optional<SessionData> findByToken(RefreshToken token);
}
