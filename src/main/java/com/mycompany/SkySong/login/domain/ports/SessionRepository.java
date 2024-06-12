package com.mycompany.SkySong.login.domain.ports;

import com.mycompany.SkySong.common.entity.Session;

public interface SessionRepository {
    void save(Session session);
}
