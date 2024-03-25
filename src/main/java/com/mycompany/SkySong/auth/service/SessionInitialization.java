package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.Session;
import org.springframework.stereotype.Service;

@Service
public class SessionCreationService {

    private final UserSessionCreator userSessionCreator;

    public SessionCreationService(UserSessionCreator userSessionCreator) {
        this.userSessionCreator = userSessionCreator;
    }

    public String initializeSession(LoginRequest request) {
        Session session = userSessionCreator.createUserSession(request);
        return session.getSessionId();
    }
}
