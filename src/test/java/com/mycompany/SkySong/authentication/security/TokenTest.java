package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TokenTest {
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private Authentication mockAuth;

    @BeforeEach
    public void setUp() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Secret = Base64.getEncoder().encodeToString(key.getEncoded());
        jwtTokenProvider = new JwtTokenProvider(base64Secret, 10L);
    }
    @Test
    void shouldGenerateValidTokenForGivenAuthentication() {
        when(mockAuth.getName()).thenReturn("testUser");

        String token = jwtTokenProvider.generateToken(mockAuth);
        assertNotNull(token);

    }
    @Test
    void shouldValidateTokenGeneratedForGivenAuthentication() {
        when(mockAuth.getName()).thenReturn("testUser");

        String token = jwtTokenProvider.generateToken(mockAuth);
        assertTrue(jwtTokenProvider.validateToken(token));
    }
    @Test
    void shouldThrowExceptionForExpiredToken() {

    }

}