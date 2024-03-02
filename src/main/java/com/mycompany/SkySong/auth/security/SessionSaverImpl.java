package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.auth.repository.SessionDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionSaverImpl implements SessionSaver {

    private final SessionDAO sessionDAO;

    public SessionSaverImpl(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    @Transactional
    public void saveSession(Session session) {
        sessionDAO.save(session);
    }
}
