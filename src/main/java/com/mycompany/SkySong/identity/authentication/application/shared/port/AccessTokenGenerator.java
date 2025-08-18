package com.mycompany.SkySong.identity.authentication.application.shared.port;

import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessToken;
import com.mycompany.SkySong.identity.authentication.application.shared.dto.AccessTokenClaims;

public interface AccessTokenGenerator {
    AccessToken generate(AccessTokenClaims claims);
}
