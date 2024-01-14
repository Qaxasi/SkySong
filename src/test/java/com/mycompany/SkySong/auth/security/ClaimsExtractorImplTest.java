package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.service.LoginService;
import com.mycompany.SkySong.testsupport.BaseIT;

import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClaimsExtractorImplTest extends BaseIT {
    @Autowired
    private ClaimsExtractor claimsExtractor;
    @Autowired
    private LoginService login;

    @Test
    void whenTokenExtracted_UsernameMatches() throws Exception {
        String token = loginAndGetToken();

        Claims claims = claimsExtractor.getClaimsFromToken(token);
        String username = claims.getSubject();

        assertEquals(username, "User");
    }

    private String loginAndGetToken() {
        return login.login(new LoginRequest("User", "Password#3"));
    }
}