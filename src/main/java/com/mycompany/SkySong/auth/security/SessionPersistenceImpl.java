package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.stereotype.Service;

@Service
public class SessionPersistenceImpl implements SessionPersistence {

    private final SessionDAO sessionDAO;

    public SessionPersistenceImpl(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    public void saveSession(Session session) {

    }
}
