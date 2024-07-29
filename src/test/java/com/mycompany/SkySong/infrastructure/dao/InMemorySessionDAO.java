package com.mycompany.SkySong.infrastructure.dao;

import com.mycompany.SkySong.domain.shared.entity.Session;
import com.mycompany.SkySong.infrastructure.persistence.dao.SessionDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemorySessionDAO implements SessionDAO {

    private final Map<String, Session> sessions = new HashMap<>();

    @Override
    public void save(Session session) {
        sessions.put(session.getSessionId(), session);
    }

    @Override
    public Optional<Session> findById(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    @Override
    public void deleteById(String sessionId) {
        sessions.remove(sessionId);
    }

    @Override
    public void deleteUserSessions(int userId) {
        sessions.entrySet().removeIf(entry -> entry.getValue().getUserId() == userId);
    }

    public void clear() {
        sessions.clear();
    }
}
