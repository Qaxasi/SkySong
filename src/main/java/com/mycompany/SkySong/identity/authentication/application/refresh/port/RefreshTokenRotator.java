package com.mycompany.SkySong.identity.authentication.application.refresh.port;

import com.mycompany.SkySong.identity.authentication.domain.Session;
import com.mycompany.SkySong.shared.result.Result;

public interface RefreshTokenRotator {
    Result<Void> rotate(String oldToken, String newToken, Session sessionData);
}
