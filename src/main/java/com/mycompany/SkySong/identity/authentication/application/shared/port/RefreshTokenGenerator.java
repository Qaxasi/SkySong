package com.mycompany.SkySong.identity.authentication.shared.port;

import com.mycompany.SkySong.identity.domain.RefreshToken;

public interface RefreshTokenGenerator {
    RefreshToken generate();
}
