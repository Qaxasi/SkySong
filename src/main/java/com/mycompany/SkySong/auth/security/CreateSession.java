package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CreateSession {
    private final TokenHasher tokenHasher;

    public CreateSession(TokenHasher tokenHasher) {
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
