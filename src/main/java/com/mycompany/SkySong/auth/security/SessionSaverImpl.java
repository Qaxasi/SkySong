package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.stereotype.Service;

@Service
public class SessionSaverImpl implements SessionSaver {

    private final SessionDAO sessionDAO;

    public SessionSaverImpl(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    public void saveSession(Session session) {
        sessionDAO.save(session);
    }
}
