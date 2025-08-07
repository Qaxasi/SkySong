package com.mycompany.SkySong.identity.authentication.shared.port;

import com.mycompany.SkySong.identity.authentication.shared.dto.AccessTokenPayload;
import com.mycompany.SkySong.identity.domain.AccessToken;

public interface AccessTokenGenerator {
    AccessToken generate(AccessTokenPayload payload);
}
