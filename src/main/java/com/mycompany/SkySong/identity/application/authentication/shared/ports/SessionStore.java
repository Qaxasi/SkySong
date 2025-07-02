package com.mycompany.SkySong.identity.application.authentication.shared.ports;

import com.mycompany.SkySong.identity.application.authentication.shared.dto.SessionData;
import com.mycompany.SkySong.identity.domain.RefreshToken;

import java.util.Optional;

public interface SessionStore {
    void save(RefreshToken token, SessionData sessionData);
    Optional<SessionData> findByToken(RefreshToken token);
    void delete(RefreshToken token);
}
