package com.mycompany.SkySong.domain.logout.ports;

public interface LogoutSessionRepository {
    void deleteById(String sessionId);
}
