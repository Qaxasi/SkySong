package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import com.mycompany.SkySong.auth.model.entity.User;
import com.mycompany.SkySong.auth.exception.SessionNotFoundException;
import com.mycompany.SkySong.auth.repository.UserDAO;
import com.mycompany.SkySong.auth.service.ApplicationMessageLoader;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionUserInfoProvider {

    private final SessionDAO sessionDAO;

    private final UserDAO userDAO;

    private final TokenHasher tokenHasher;

    private final ApplicationMessageLoader message;

    public SessionUserInfoProvider(SessionDAO sessionDAO,
                                   UserDAO userDAO,
                                   TokenHasher tokenHasher,
                                   ApplicationMessageLoader message) {
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
        this.tokenHasher = tokenHasher;
        this.message = message;
    }

    public String getUsernameForSession(String sessionId) {
        return Optional.of(sessionId)
                .map(tokenHasher::generateHashedToken)
                .flatMap(sessionDAO::findById)
                .map(Session::getUserId)
                .flatMap(userDAO::findById)
                .map(User::getUsername)
                .orElseThrow(() -> new SessionNotFoundException(message.getMessage("session.not.found", sessionId)));
    }
}
