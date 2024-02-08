package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.repository.UserDAO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionUserInfoProviderImpl implements SessionUserInfoProvider {

    private final SessionDAO sessionDAO;

    private final UserDAO userDAO;

    private final SecureTokenGenerator tokenGenerator;

    public SessionUserInfoProviderImpl(SessionDAO sessionDAO, UserDAO userDAO, SecureTokenGenerator tokenGenerator) {
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public String getUsernameForSession(String sessionId) {
        return Optional.of(sessionId)
                .map(tokenGenerator::generateHashedToken)
                .flatMap(sessionDAO::findById)
                .map(Session::getUserId)
                .map(Long::valueOf)
                .flatMap(userDAO::findById)
                .map(User::getUsername)
                .orElse(null);
    }
}
