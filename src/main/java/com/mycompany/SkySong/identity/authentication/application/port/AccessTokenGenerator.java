package com.mycompany.SkySong.identity.authentication.application.port;

import com.mycompany.SkySong.identity.authentication.application.dto.AccessToken;
import com.mycompany.SkySong.identity.authentication.application.dto.AccessTokenClaims;

public interface AccessTokenGenerator {
    AccessToken generate(AccessTokenClaims claims);
}
