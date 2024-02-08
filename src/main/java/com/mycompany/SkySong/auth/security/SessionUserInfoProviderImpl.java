package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.repository.SessionDAO;
import com.mycompany.SkySong.shared.repository.UserDAO;
import org.springframework.stereotype.Service;

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
        return null;
    }
}
