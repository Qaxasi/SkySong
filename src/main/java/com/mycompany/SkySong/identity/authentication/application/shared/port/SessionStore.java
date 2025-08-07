package com.mycompany.SkySong.identity.authentication.shared.port;

import com.mycompany.SkySong.identity.authentication.shared.dto.SessionData;
import com.mycompany.SkySong.identity.domain.RefreshToken;

import java.util.Optional;

public interface SessionStore {
    void save(RefreshToken token, SessionData sessionData);
    Optional<SessionData> findByToken(RefreshToken token);
    void delete(RefreshToken token);
}
