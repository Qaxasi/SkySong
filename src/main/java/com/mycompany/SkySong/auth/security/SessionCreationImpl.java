package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SessionCreationImpl implements SessionCreation {

    private final TokenGenerator tokenGenerator;
    private final TokenHasher tokenHasher;

    public SessionCreationImpl(TokenGenerator tokenGenerator, TokenHasher tokenHasher) {
        this.tokenGenerator = tokenGenerator;
        this.tokenHasher = tokenHasher;
    }

    @Override
    public Session createSession(Integer userID) {
        String token = tokenGenerator.generateToken();
        String hashedToken = tokenHasher.generateHashedToken(token);

        Session session = new Session();
        session.setSessionId(hashedToken);
        session.setUserId(userID);
        session.setCreateAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));

        return session;
    }
}
