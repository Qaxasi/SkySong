package com.mycompany.SkySong.adapter.security;

import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class JwtTokenTest {

    private JwtTokenManager jwtManager;

    @BeforeEach
    void setup() {
        String secretKey = "-c4iY6yOly4Io2kfebbk/sGdU6Lq1lSS2DX7XvNr0agM=";
        long jwtExpiration = 100000L;
        long refreshJwtExpiration = 200000L;
        jwtManager = new JwtTokenManager(secretKey, jwtExpiration, refreshJwtExpiration);
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

    private String extractUsername(String token) {
        return jwtManager.extractUsername(token);
    }

    private List<String> extractRoles(String token) {
        return jwtManager.extractRoles(token);
    }
}
