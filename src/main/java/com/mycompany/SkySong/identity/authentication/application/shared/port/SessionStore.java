package com.mycompany.SkySong.identity.authentication.application.shared.port;

import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.shared.domain.UserTag;
import com.mycompany.SkySong.shared.result.Result;

public interface SessionStore {
    Result<Void> save(UserTag userTag, String refreshToken, Session session, long ttlSeconds);
    Result<Session> findByRefreshToken(UserTag userTag, String token);
    Result<Void> rotateRefreshToken(UserTag userTag, String oldToken, String newToken,
                                    Session session, long ttlSeconds);
    Result<Void> deleteUserSessions(UserTag userTag);
}
