package com.mycompany.SkySong.logout.domain.ports;

import com.mycompany.SkySong.common.dao.SessionDAO;
public interface SessionRepositoryPort extends SessionDAO {
    void deleteById(String sessionId);
}
