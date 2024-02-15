package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.security.TokenGenerator;
import com.mycompany.SkySong.auth.security.TokenHasher;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SessionTestHelper {

    private final TokenHasher tokenHasher;
    private final TokenGenerator tokenGenerator;

    public SessionTestHelper(TokenHasher tokenHasher, TokenGenerator tokenGenerator) {
        this.tokenHasher = tokenHasher;
        this.tokenGenerator = tokenGenerator;
    }

    public String createExpiredSession(Integer userID) {
        String token = tokenGenerator.generateToken();
        String hashedToken = tokenHasher.generateHashedToken(token);

        Session session = new Session();
        session.setSessionId(hashedToken);
        session.setUserId(userID);
        session.setCreateAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis() + (-10000)));

        sessionDAO.save(session);
        return token;
    }
}
