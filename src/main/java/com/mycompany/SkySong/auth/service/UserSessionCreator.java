package com.mycompany.SkySong.auth.service;
import com.mycompany.SkySong.auth.exception.UserNotFoundException;
import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import com.mycompany.SkySong.auth.repository.UserDAO;
import com.mycompany.SkySong.auth.security.SessionCreation;
import com.mycompany.SkySong.auth.security.TokenGenerator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserSessionCreator {

    private final SessionCreation sessionCreation;
    private final UserAuthentication userAuth;
    private final TokenGenerator tokenGenerator;
    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;

    public UserSessionCreator(SessionCreation sessionCreation,
                              UserAuthentication userAuth,
                              TokenGenerator tokenGenerator,
                              SessionDAO sessionDAO,
                              UserDAO userDAO) {
        this.sessionCreation = sessionCreation;
        this.userAuth = userAuth;
        this.tokenGenerator = tokenGenerator;
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
    }

    public Session createUserSession(LoginRequest request) {
        String token = tokenGenerator.generateToken();
        Authentication auth = userAuth.authenticateUser(request);
        User user = userDAO.findByUsername(auth.getName()).orElseThrow(
                () -> new UserNotFoundException("User not found with username :" + auth.getName()));

        Session session = sessionCreation.createSession(token, user.getId());
        sessionDAO.save(session);

        return session;
    }
}
