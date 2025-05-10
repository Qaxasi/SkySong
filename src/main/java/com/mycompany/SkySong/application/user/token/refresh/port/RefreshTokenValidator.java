package com.mycompany.SkySong.application.user.token.refresh.port;

public interface RefreshTokenValidator {
    void validateToken(String token);
}
