package com.mycompany.SkySong.application.user.token.refresh.ports;

public interface RefreshTokenValidator {
    void validateToken(String token);
}
