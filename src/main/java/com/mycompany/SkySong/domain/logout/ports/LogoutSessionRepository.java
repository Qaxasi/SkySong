package com.mycompany.SkySong.logout.domain.ports;

public interface LogoutSessionRepository {
    void deleteById(String sessionId);
}
