package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.common.exception.UserNotFoundException;
import com.mycompany.SkySong.common.utils.TokenHasher;
import com.mycompany.SkySong.login.application.dto.LoginRequest;
import com.mycompany.SkySong.login.domain.ports.UserRepositoryPort;
import com.mycompany.SkySong.user.Session;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.user.SessionDAO;
import com.mycompany.SkySong.user.UserDAO;
import org.springframework.security.core.Authentication;

import java.util.Date;

public class UserSessionCreator {
    private final TokenHasher tokenHasher;
    private final SessionCreation sessionCreation;
    private final UserRepositoryPort userRepository;
    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;

    public UserSessionCreator(TokenHasher tokenHasher, SessionCreation sessionCreation,
                              UserRepositoryPort userRepository,
                              SessionDAO sessionDAO,
                              UserDAO userDAO) {
        this.tokenHasher = tokenHasher;
        this.sessionCreation = sessionCreation;
        this.userRepository = userRepository;
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
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
