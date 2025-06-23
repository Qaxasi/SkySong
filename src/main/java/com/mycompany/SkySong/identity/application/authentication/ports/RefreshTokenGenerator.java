package com.mycompany.SkySong.application.user.authentication.ports;

import com.mycompany.SkySong.application.user.authentication.dto.RefreshToken;

public interface RefreshTokenGenerator {
    RefreshToken generate();
}
