package com.mycompany.SkySong.adapter.security;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetails;
import com.mycompany.SkySong.testsupport.auth.common.TestJwtTokenGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenTest {

    private JwtTokenManager jwtManager;
    private TestJwtTokenGenerator testTokenGenerator;

    @BeforeEach
    void setup() {
        String secretKey = "wJ4ds7VbRmFHRP4fX5QbJmTcYZv5P1ZkVN7skO4id8E=s";
        long jwtExpiration = 600000L;
        long refreshJwtExpiration = 86400000L;

        testTokenGenerator = new TestJwtTokenGenerator(secretKey, jwtExpiration, refreshJwtExpiration);
        jwtManager = new JwtTokenManager(secretKey, jwtExpiration, refreshJwtExpiration);
    }

    @Test
    void whenGeneratedRefreshToken_TokenHasCorrectExpirationTime() {
        String token = generateRefreshToken();
        Claims claims = extractClaims(token);
        Date expiration = claims.getExpiration();
        long expirationTime = expiration.getTime();
        long expectedExpiration = System.currentTimeMillis() + 86400000;

        assertThat(expirationTime <= expectedExpiration &&
                expirationTime >= expectedExpiration - 1000).isTrue();
    }

    @Test
    void whenGeneratedRefreshToken_TokenContainsCorrectUsername() {
        String token = generateRefreshTokenForUserWithUsername("Alex");
        Claims claims = extractClaims(token);
        assertThat(claims.getSubject()).isEqualTo("Alex");
    }

    @Test
    void whenGeneratedAccessToken_TokenHasCorrectExpirationTime() {
        String token = generateToken();
        Claims claims = extractClaims(token);
        Date expiration = claims.getExpiration();
        long expirationTime = expiration.getTime();
        long expectedExpirationTime = System.currentTimeMillis() + 600000;

        assertThat(expirationTime <= expectedExpirationTime &&
                expirationTime >= expectedExpirationTime - 1000).isTrue();
    }

    @Test
    void whenGeneratedAccessToken_TokenContainsCorrectSubject() {
        String token = generateTokenForUserWithUsername("Alex");
        Claims claims = extractClaims(token);
        assertThat(claims.getSubject()).isEqualTo("Alex");
    }

    @Test
    void whenGeneratedAccessToken_TokenContainsExpectedClaims() {
        List<String> roles = List.of("ROLE_USER");
        String token = generateTokenForUserWithRoles(roles);
        Claims claims = extractClaims(token);

        assertThat(claims.get("roles", List.class)).isEqualTo(roles);
    }

    @Test
    void whenExtractUsernameFromGeneratedToken_UsernameIsCorrect() {
        String token = generateTokenForUserWithUsername("Alex");
        String username = extractUsername(token);

        assertThat(username).isEqualTo("Alex");
    }

    @Test
    void whenExtractedRolesFromGeneratedToken_RolesAreCorrect() {
        String token = generateTokenForUserWithRoles(List.of("ROLE_USER"));
        List<String> roles = extractRoles(token);

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

    private String generateToken() {
        return jwtManager.generateToken(getUserDetailsForAccessToken());
    }

    private String generateRefreshTokenForUserWithUsername(String username) {
        return jwtManager.generateRefreshToken(getUserDetailsForRefreshTokenWithUsername(username));
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

    private CustomUserDetails getUserDetailsForAccessToken() {
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new CustomUserDetails(1, "Alex", "alex@mail.mail", "Password#3", authorities);
    }

    private CustomUserDetails getUserDetailsForRefreshToken() {
        return new CustomUserDetails(1, "Alex", "alex@mail.mail", "Password#3", new HashSet<>());
    }

    private CustomUserDetails getUserDetailsForRefreshTokenWithUsername(String username) {
        return new CustomUserDetails(1, username, "alex@mail.mail", "Password#3", new HashSet<>());
    }

    private String generateRefreshToken() {
        return jwtManager.generateRefreshToken(getUserDetailsForRefreshToken());
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
