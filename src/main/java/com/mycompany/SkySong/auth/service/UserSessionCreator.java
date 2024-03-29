package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.UserNotFoundException;
import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import com.mycompany.SkySong.auth.repository.UserDAO;
import com.mycompany.SkySong.auth.security.SessionCreation;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserSessionCreator {

    private final SessionCreation sessionCreation;
    private final UserAuthentication userAuth;
    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;

    public UserSessionCreator(SessionCreation sessionCreation,
                              UserAuthentication userAuth,
                              SessionDAO sessionDAO,
                              UserDAO userDAO) {
        this.sessionCreation = sessionCreation;
        this.userAuth = userAuth;
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
    }

    @Transactional
    public void createUserSession(LoginRequest request, String sessionToken) {
        Authentication auth = userAuth.authenticateUser(request);
        User user = userDAO.findByUsername(auth.getName()).orElseThrow(
                () -> new UserNotFoundException("User not found with username :" + auth.getName()));

        Session session = sessionCreation.createSession(sessionToken, user.getId());
        sessionDAO.save(session);
    }
}
