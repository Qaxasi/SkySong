package com.mycompany.SkySong.identity.application.port;

import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.core.result.Result;

public interface RefreshTokenGenerator {
    Result<RefreshToken> generate();
}