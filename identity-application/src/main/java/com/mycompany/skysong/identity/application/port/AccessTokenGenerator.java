package com.mycompany.SkySong.identity.application.port;

import com.mycompany.SkySong.identity.application.model.AccessToken;
import com.mycompany.SkySong.identity.application.model.AccessTokenClaims;

public interface AccessTokenGenerator {
    AccessToken generate(AccessTokenClaims claims);
}
