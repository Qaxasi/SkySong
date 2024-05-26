package com.mycompany.SkySong.logout.domain.service;

import com.mycompany.SkySong.common.utils.TokenHasher;
import com.mycompany.SkySong.common.dao.SessionDAO;
import org.springframework.stereotype.Service;

@Service
public class SessionDeletionService {

    private final SessionDAO sessionDAO;

    private final TokenHasher tokenHasher;

    public SessionDeletionService(SessionDAO sessionDAO,
                                  TokenHasher tokenHasher) {
        this.sessionDAO = sessionDAO;
        this.tokenHasher = tokenHasher;
    }
    
    public void deleteSession(String sessionId) {
        String hashedSessionId = tokenHasher.hashToken(sessionId);
        sessionDAO.deleteById(hashedSessionId);
    }
}
