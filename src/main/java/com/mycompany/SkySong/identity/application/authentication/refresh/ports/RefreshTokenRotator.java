package com.mycompany.SkySong.identity.application.authentication.refresh.ports;

import com.mycompany.SkySong.identity.application.authentication.shared.dto.SessionData;
import com.mycompany.SkySong.identity.domain.RefreshToken;

public interface RefreshTokenRotator {
    void rotate(RefreshToken oldToken, RefreshToken newToken, SessionData sessionData);
}
