package com.mycompany.SkySong.domain.login.ports;

import com.mycompany.SkySong.domain.shared.entity.Session;

public interface LoginSessionRepository {
    void save(Session session);
}
