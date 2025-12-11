package com.mycompany.skysong.identity.application.user.authentication.port;

import com.mycompany.skysong.identity.application.user.authentication.model.AccessToken;
import com.mycompany.skysong.identity.application.user.authentication.model.AccessTokenClaims;

public interface AccessTokenGenerator {
    AccessToken generate(AccessTokenClaims claims);
}
