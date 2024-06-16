package com.mycompany.SkySong.logout.domain.ports;

public interface SessionRepository {
    void deleteById(String sessionId);
}
