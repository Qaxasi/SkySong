package com.mycompany.skysong.identity.application.authentication.port;

import com.mycompany.skysong.identity.domain.RefreshToken;
import com.mycompany.skysong.core.result.Result;

public interface RefreshTokenGenerator {
    Result<RefreshToken> generate();
}