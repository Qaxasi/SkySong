package com.mycompany.SkySong.login.domain.ports;

import com.mycompany.SkySong.common.entity.Session;

public interface LoginSessionRepository {
    void save(Session session);
}
