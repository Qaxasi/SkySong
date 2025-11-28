package com.mycompany.SkySong.identity.application.port;

import com.mycompany.SkySong.identity.application.dto.AccessToken;
import com.mycompany.SkySong.identity.application.dto.AccessTokenClaims;

public interface AccessTokenGenerator {
    AccessToken generate(AccessTokenClaims claims);
}
