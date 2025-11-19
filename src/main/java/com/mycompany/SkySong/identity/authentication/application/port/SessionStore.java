package com.mycompany.SkySong.identity.authentication.application.port;

import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.identity.authentication.domain.UserTag;
import com.mycompany.SkySong.shared.result.Result;
import com.mycompany.SkySong.shared.result.Unit;

import java.time.Duration;

public interface SessionStore {
    Result<Unit> saveSession(UserTag userTag, RefreshToken token, Session session, Duration ttlSeconds);
    Result<Session> findBy(UserTag userTag, RefreshToken token);
    Result<Unit> rotateSession(UserTag userTag, RefreshToken oldToken, RefreshToken newToken,
                               Session session, Duration ttlSeconds);
}
