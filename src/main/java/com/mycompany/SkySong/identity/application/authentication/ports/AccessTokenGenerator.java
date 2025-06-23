package com.mycompany.SkySong.application.user.authentication.ports;

import com.mycompany.SkySong.application.user.authentication.dto.AccessToken;
import com.mycompany.SkySong.application.user.authentication.dto.AccessTokenPayload;

public interface AccessTokenGenerator {
    AccessToken generate(AccessTokenPayload payload);
}
