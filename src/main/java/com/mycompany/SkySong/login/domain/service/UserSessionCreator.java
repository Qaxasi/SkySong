package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.common.utils.TokenHasher;
import com.mycompany.SkySong.user.Session;
import java.util.Date;

public class UserSessionCreator {

    private final TokenHasher tokenHasher;

    public UserSessionCreator(TokenHasher tokenHasher) {
        this.tokenHasher = tokenHasher;

    }

    public void createUserSession(String sessionToken, int userId) {
        Session session = createSession(sessionToken, userId);
        sessionDAO.save(session);
    }

    private Session createSession(String token, Integer userId) {
        String hashedToken = tokenHasher.generateHashedToken(token);

        Session session = new Session();
        session.setSessionId(hashedToken);
        session.setUserId(userId);
        session.setCreateAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));

        return session;
    }
}
