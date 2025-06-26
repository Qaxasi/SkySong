package com.mycompany.SkySong.identity.application.authentication.ports;

import com.mycompany.SkySong.identity.domain.RefreshToken;

public interface RefreshTokenGenerator {
    RefreshToken generate();
}
