package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.TokenGeneratorHelper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClaimsExtractorImplTest extends BaseIT {
    @Autowired
    private ClaimsExtractor claimsExtractor;
    @Autowired
    private TokenGeneratorHelper tokenHelper;
    @Test
    void whenTokenExtracted_UsernameMatches() {
        //given
        String token = loginAndGetToken();

        //when
        Claims claims = claimsExtractor.getClaimsFromToken(token);
        String username = claims.getSubject();

        //then
        assertEquals(username, "User");
    }
}