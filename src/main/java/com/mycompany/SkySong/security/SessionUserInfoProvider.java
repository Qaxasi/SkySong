package com.mycompany.SkySong.common.security;

import com.mycompany.SkySong.user.Session;
import com.mycompany.SkySong.user.SessionDAO;
import com.mycompany.SkySong.common.entity.User;
import com.mycompany.SkySong.user.UserDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionUserInfoProvider {

    private final SessionDAO sessionDAO;

    private final UserDAO userDAO;

    public SessionUserInfoProvider(SessionDAO sessionDAO,
                                   UserDAO userDAO) {
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
    }

    public String getUsernameForSession(String sessionId) {
        return Optional.of(sessionId)
                .flatMap(sessionDAO::findById)
                .map(Session::getUserId)
                .flatMap(userDAO::findById)
                .map(User::getUsername)
                .orElseThrow(() -> new SessionNotFoundException("No user associated with session ID:" + sessionId));
    }
}
