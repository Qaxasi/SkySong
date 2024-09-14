package com.mycompany.SkySong.adapter.security;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetails;
import com.mycompany.SkySong.testsupport.auth.common.TestJwtTokenGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenTest {

    private JwtTokenManager jwtManager;
    private TestJwtTokenGenerator testTokenGenerator;
    private String secretKey;

    @BeforeEach
    void setup() {
        secretKey = "wJ4ds7VbRmFHRP4fX5QbJmTcYZv5P1ZkVN7skO4id8E=s";
        long jwtExpiration = 600000L;
        long refreshJwtExpiration = 86400000L;

        testTokenGenerator = new TestJwtTokenGenerator(secretKey, jwtExpiration, refreshJwtExpiration);
        jwtManager = new JwtTokenManager(secretKey, jwtExpiration, refreshJwtExpiration);
    }

    @Test
    void whenGeneratedTokenForUser_ExtractedUsernameIsCorrect() {
        String token = generateTokenForUserWithUsername("Alex");
        String username = extractUsername(token); // nie używać implementacji ?

        assertThat(username).isEqualTo("Alex");
    }

    @Test
    void whenGeneratedTokenWithRole_ExtractedRolesAreCorrect() {
        String token = generateTokenForUserWithRoles(List.of("ROLE_USER"));
        List<String> roles = extractRoles(token); //nie uzywać implementacji ?

        assertThat(List.of("ROLE_USER")).isEqualTo(roles);
    }

    @Test
    void whenTokenIsValid_ValidationReturnsTrue() {
        String token = generateValidAccessToken();
        assertThat(isTokenValid(token)).isTrue();
    }

    @Test
    void whenTokenIsEmpty_ValidationFails() {
        String emptyToken = "";
        assertThat(isTokenValid(emptyToken)).isFalse();
    }

    @Test
    void whenTokenIsMalformed_ValidationFails() {
        String malformedToken = "token.is.malformed";
        assertThat(isTokenValid(malformedToken)).isFalse();
    }

    @Test
    void whenTokenIsExpired_ValidationFails() {
        String expiredToken = createExpiredToken();
        assertThrows(ExpiredJwtException.class, () -> isTokenValid(expiredToken));
    }

    @Test
    void whenTokenIsSignedWithUnsupportedAlgorithm_ValidationFails() {
        String token = createUnsupportedToken();
        assertThat(isTokenValid(token)).isFalse();
    }

    private String createUnsupportedToken() {
        return testTokenGenerator.createUnsupportedToken();
    }

    private String createExpiredToken() {
        return testTokenGenerator.createExpiredToken();
    }

    private boolean isTokenValid(String token) {
        return jwtManager.isTokenValid(token);
    }

    private String generateTokenForUserWithUsername(String username) {
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        CustomUserDetails customUserDetails = new CustomUserDetails(1, username, "alex@mail.mail", "Password#3", authorities);
        return jwtManager.generateToken(customUserDetails);
    }

    private String generateTokenForUserWithRoles(List<String> roles) {
        Set<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        CustomUserDetails customUserDetails = new CustomUserDetails(1, "Alex", "alex@mail.mail", "Password#3", authorities);
        return jwtManager.generateToken(customUserDetails);
    }

    private CustomUserDetails getCustomUserDetails() {
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new CustomUserDetails(1, "Alex", "alex@mail.mail", "Password#3", authorities);
    }

    private String generateValidAccessToken() {
        return testTokenGenerator.generateValidAccessToken();
    }

    private String extractUsername(String token) {
        return jwtManager.extractUsername(token);
    }

    private List<String> extractRoles(String token) {
        return jwtManager.extractRoles(token);
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getTestSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getTestSignKey() {
        return testTokenGenerator.generateSignKey();
    }
}
