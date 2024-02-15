package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.TokenHasher;
import org.springframework.stereotype.Component;

@Component
public class SessionTestHelper {

    private final TokenHasher tokenHasher;

    public SessionTestHelper(TokenHasher tokenHasher) {
        this.tokenHasher = tokenHasher;
    }
}
