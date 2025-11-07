package com.mycompany.SkySong.identity.authentication.application.port;

import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.shared.result.Result;

public interface RefreshTokenGenerator {
    Result<RefreshToken> generate();
}