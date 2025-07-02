package com.mycompany.SkySong.identity.application.authentication.shared.ports;

import com.mycompany.SkySong.identity.application.authentication.shared.dto.AccessTokenPayload;
import com.mycompany.SkySong.identity.domain.AccessToken;

public interface AccessTokenGenerator {
    AccessToken generate(AccessTokenPayload payload);
}
