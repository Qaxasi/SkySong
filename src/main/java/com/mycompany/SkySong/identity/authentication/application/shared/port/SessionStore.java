package com.mycompany.SkySong.identity.authentication.application.shared.port;

import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.shared.domain.UserTag;
import com.mycompany.SkySong.shared.result.Result;

public interface SessionStore {
    Result<Void> save(UserTag userTag, RefreshToken token, Session session, long ttlSeconds);
    Result<Session> findByRefreshToken(UserTag userTag, RefreshToken token);
    Result<Void> rotateRefreshToken(UserTag userTag, RefreshToken oldToken, RefreshToken newToken,
                                    Session session, long ttlSeconds);
    Result<Void> deleteUserSessions(UserTag userTag);
}
