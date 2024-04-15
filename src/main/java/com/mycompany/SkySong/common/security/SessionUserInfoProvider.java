package com.mycompany.SkySong.common.security;

import com.mycompany.SkySong.common.utils.TokenHasher;
import com.mycompany.SkySong.user.Session;
import com.mycompany.SkySong.user.SessionDAO;
import com.mycompany.SkySong.user.User;
import com.mycompany.SkySong.user.UserDAO;
import com.mycompany.SkySong.common.utils.ApplicationMessageLoader;
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
