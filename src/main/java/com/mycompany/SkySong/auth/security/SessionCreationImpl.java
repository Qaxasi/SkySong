package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SessionCreationImpl implements SessionCreation {

    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private SecureTokenGenerator tokenGenerator;

    @Override
    public String createSession(Integer userID) {
        String token = tokenGenerator.generateToken();
        String hashedToken = tokenGenerator.generateHashedToken(token);

        Session session = new Session();
        session.setSessionId(hashedToken);
        session.setUserId(userID);
        session.setCreateAt(new Date());
        session.setExpiresAt(new Date(System.currentTimeMillis() + 1000));

        sessionDAO.save(session);
        return token;
    }
}
