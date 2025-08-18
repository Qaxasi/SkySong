package com.mycompany.SkySong.identity.authentication.application.shared.port;

import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.shared.result.Result;

public interface SessionStore {
    Result<Void> save(String token, Session session);
    Result<Session> findByToken(String token);
    Result<Void> delete(String token);
}
