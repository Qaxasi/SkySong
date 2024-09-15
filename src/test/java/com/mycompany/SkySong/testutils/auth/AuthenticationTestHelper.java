package com.mycompany.SkySong.testutils.auth;

import com.mycompany.SkySong.adapter.login.dto.LoginRequest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class AuthenticationTestHelper {

    private final TestRestTemplate restTemplate;

    public AuthenticationTestHelper(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String loginRegularUser() {
        LoginRequest user = new LoginRequest("Regular", "Password#3");
        return loginAndGetJwtToken(user);
    }

    public String loginAdminUser() {
       LoginRequest admin = new LoginRequest("Admin", "Password#3");
       return loginAndGetJwtToken(admin);
    }

    private String loginAndGetJwtToken(LoginRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/auth/login", entity, String.class);

            String cookieHeader = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

            return extractJwtTokenFromCookie(cookieHeader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to login");
        }
    }

    private String extractJwtTokenFromCookie(String cookie) {
        if (cookie == null) {
            throw new AssertionError("Cookie header is null; JWT token not found.");
        }

        String[] parts = cookie.split(";");
        for (String part : parts) {
            if (part.trim().startsWith("jwtToken=")) {
                return part.trim().substring("jwtToken=".length());
            }
        }
        throw new AssertionError("JWT token not found within the cookie.");
    }
}
