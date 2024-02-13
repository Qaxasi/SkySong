package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.repository.UserDAO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionUserInfoProviderImpl implements SessionUserInfoProvider {

    private final SessionDAO sessionDAO;

    private final UserDAO userDAO;

    private final TokenHasher tokenHasher;

    public SessionUserInfoProviderImpl(SessionDAO sessionDAO, UserDAO userDAO, TokenHasher tokenHasher) {
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
        this.tokenHasher = tokenHasher;
    }

    @Override
    public String getUsernameForSession(String sessionId) {
        return Optional.of(sessionId)
                .map(tokenHasher::generateHashedToken)
                .flatMap(sessionDAO::findById)
                .map(Session::getUserId)
                .map(Long::valueOf)
                .flatMap(userDAO::findById)
                .map(User::getUsername)
                .orElseThrow(() -> new UsernameNotFoundException("No user associated with session ID: " + sessionId));
    }
}
