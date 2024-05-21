package com.mycompany.SkySong.user.delete.domain.ports;

import com.mycompany.SkySong.common.dao.SessionDAO;

public interface SessionRepositoryPort extends SessionDAO {
    void deleteUserSessions( int userId);
}
