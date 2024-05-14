package com.mycompany.SkySong.login.domain.ports;

import com.mycompany.SkySong.user.Session;
import com.mycompany.SkySong.user.SessionDAO;

public interface SessionRepositoryPort extends SessionDAO {
    void save(Session session);
}
