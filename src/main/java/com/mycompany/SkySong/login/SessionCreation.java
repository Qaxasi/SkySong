package com.mycompany.SkySong.login;

import com.mycompany.SkySong.common.utils.TokenHasher;
import com.mycompany.SkySong.user.Session;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SessionCreation {
    private final TokenHasher tokenHasher;

    public SessionCreation(TokenHasher tokenHasher) {
        this.tokenHasher = tokenHasher;
    }

    public Session createSession(String token, Integer userId) {
        String hashedToken = tokenHasher.generateHashedToken(token);

        Session session = new Session();
        session.setSessionId(hashedToken);
        session.setUserId(userId);
        session.setCreateAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));

        return session;
    }
}
