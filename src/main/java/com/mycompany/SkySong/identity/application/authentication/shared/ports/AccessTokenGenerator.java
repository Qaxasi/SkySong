package com.mycompany.SkySong.identity.application.authentication.ports;

import com.mycompany.SkySong.identity.application.authentication.dto.AccessTokenPayload;
import com.mycompany.SkySong.identity.domain.AccessToken;

public interface AccessTokenGenerator {
    AccessToken generate(AccessTokenPayload payload);
}
