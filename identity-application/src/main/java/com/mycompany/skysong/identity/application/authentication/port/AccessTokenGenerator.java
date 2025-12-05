package com.mycompany.skysong.identity.application.authentication.port;

import com.mycompany.skysong.identity.application.authentication.model.AccessToken;
import com.mycompany.skysong.identity.application.authentication.model.AccessTokenClaims;

public interface AccessTokenGenerator {
    AccessToken generate(AccessTokenClaims claims);
}
