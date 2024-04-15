package com.mycompany.SkySong.logout;

import com.mycompany.SkySong.common.utils.TokenHasher;
import com.mycompany.SkySong.user.SessionDAO;
import org.springframework.stereotype.Service;

@Service
public class SessionDeletion {

    private final SessionDAO sessionDAO;

    private final TokenHasher tokenHasher;

    public SessionDeletion(SessionDAO sessionDAO, TokenHasher tokenHasher) {
        this.sessionDAO = sessionDAO;
        this.tokenHasher = tokenHasher;
    }
    
    public void deleteSession(String sessionId) {
        String hashedSessionId = tokenHasher.generateHashedToken(sessionId);
        sessionDAO.deleteById(hashedSessionId);
    }
}
