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
    }

    @Override
    public Optional<Session> findById(String sessionId) {
    }

    @Override
    public void deleteById(String sessionId) {
    }

    @Override
    public void deleteUserSessions(int userId) {
    }
}
