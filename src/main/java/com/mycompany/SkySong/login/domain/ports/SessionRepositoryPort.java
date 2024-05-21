package com.mycompany.SkySong.login.domain.ports;

import com.mycompany.SkySong.common.entity.Session;
import com.mycompany.SkySong.common.dao.SessionDAO;

public interface SessionRepositoryPort extends SessionDAO {
    void save(Session session);
}
