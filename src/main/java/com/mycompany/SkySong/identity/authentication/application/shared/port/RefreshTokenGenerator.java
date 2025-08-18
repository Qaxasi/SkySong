package com.mycompany.SkySong.identity.authentication.application.shared.port;

import com.mycompany.SkySong.identity.authentication.domain.RefreshToken;
import com.mycompany.SkySong.shared.result.Result;

public interface RefreshTokenGenerator {
    Result<RefreshToken> generate();
}
