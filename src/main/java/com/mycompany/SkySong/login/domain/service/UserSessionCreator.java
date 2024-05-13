package com.mycompany.SkySong.login.domain.service;

import com.mycompany.SkySong.common.exception.UserNotFoundException;
import com.mycompany.SkySong.login.application.dto.LoginRequest;
import com.mycompany.SkySong.login.domain.ports.UserRepositoryPort;
import com.mycompany.SkySong.user.Session;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.user.SessionDAO;
import com.mycompany.SkySong.user.UserDAO;
import org.springframework.security.core.Authentication;

public class UserSessionCreator {

    private final SessionCreation sessionCreation;
    private final UserRepositoryPort userRepository;
    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;

    public UserSessionCreator(SessionCreation sessionCreation,
                              UserRepositoryPort userRepository,
                              SessionDAO sessionDAO,
                              UserDAO userDAO) {
        this.sessionCreation = sessionCreation;
        this.userRepository = userRepository;
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
    }

    public void createUserSession(LoginRequest request, String sessionToken) {
        Authentication auth = userAuth.authenticateUser(request);
        User user = userDAO.findByUsername(auth.getName()).orElseThrow(
                () -> new UserNotFoundException("User not found with username :" + auth.getName()));

        Session session = sessionCreation.createSession(sessionToken, user.getId());
        sessionDAO.save(session);
    }
}
