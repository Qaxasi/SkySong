package com.mycompany.skysong.identity.application.user.authentication.port;

import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.identity.domain.Session;
import com.mycompany.skysong.identity.domain.UserTag;
import com.mycompany.skysong.core.result.Result;
import com.mycompany.skysong.core.result.Unit;

import java.time.Duration;

public interface SessionStore {
    Result<Unit> saveSession(UserTag userTag,
                             RefreshToken token,
                             Session session,
                             Duration ttl);
    Result<Session> findSession(UserTag userTag,
                                RefreshToken token);
    Result<Unit> rotateSession(UserTag userTag,
                               RefreshToken oldToken,
                               RefreshToken newToken,
                               Session newSession,
                               Duration ttl);
}
