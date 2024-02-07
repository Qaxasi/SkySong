package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SessionValidationImpl implements SessionValidation {

    @Autowired
    private SessionDAO sessionDAO;

    @Autowired
    private SecureTokenGenerator tokenGenerator;

    @Override
    public boolean validateSession(String sessionId) {
        String hashedSessionId = tokenGenerator.generateHashedToken(sessionId);
        Session session = sessionDAO.findById(hashedSessionId).orElse(null);

        return session != null && session.getExpiresAt().after(new Date());
    }
}
