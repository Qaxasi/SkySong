package com.mycompany.SkySong.identity.authentication.application.shared.port;

import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.shared.result.Result;

public interface SessionStore {
    Result<Void> save(String userTag, String refreshToken, Session session, long ttlSeconds);
    Result<Session> findByRefreshToken(String userTag, String token);
    Result<Void> rotateRefreshToken(String userTag, String oldToken, String newToken,
                                    Session session, long ttlSeconds);
    Result<Void> deleteUserSessions(String userTag);
}
