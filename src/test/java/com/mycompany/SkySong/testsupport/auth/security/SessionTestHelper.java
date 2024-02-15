package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.TokenGenerator;
import com.mycompany.SkySong.auth.security.TokenHasher;
import org.springframework.stereotype.Component;

@Component
public class SessionTestHelper {

    private final TokenHasher tokenHasher;
    private final TokenGenerator tokenGenerator;

    public SessionTestHelper(TokenHasher tokenHasher, TokenGenerator tokenGenerator) {
        this.tokenHasher = tokenHasher;
        this.tokenGenerator = tokenGenerator;
    }

}
