package com.mycompany.SkySong.auth.service;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import com.mycompany.SkySong.auth.repository.UserDAO;
import com.mycompany.SkySong.auth.security.SessionCreation;
import com.mycompany.SkySong.auth.security.TokenGenerator;
import org.springframework.stereotype.Service;

@Service
public class UserSessionCreator {

    private final SessionCreation sessionCreation;
    private final UserAuthentication userAuth;
    private final TokenGenerator tokenGenerator;
    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;

    public UserSessionCreator(SessionCreation sessionCreation, UserAuthentication userAuth, TokenGenerator tokenGenerator, SessionDAO sessionDAO, UserDAO userDAO) {
        this.sessionCreation = sessionCreation;
        this.userAuth = userAuth;
        this.tokenGenerator = tokenGenerator;
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
    }
}
