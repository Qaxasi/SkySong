package com.mycompany.SkySong.adapter.refreshToken.controller;

import com.mycompany.SkySong.adapter.exception.response.ErrorResponse;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.mycompany.SkySong.adapter.refreshToken.service.JwtTokenExample.generateValidRefreshToken;
import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenControllerTest extends BaseIT {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    private UserFixture userFixture;

    @BeforeEach
    void setup() {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
    }

    @Test
    void whenValidTokenProvided_StatusOk() {
        userFixture.createUserWithUsername("Alex");
        String refreshToken = generateValidRefreshToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "refreshToken=" + refreshToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/refresh-token", entity, ApiResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void whenTokenRefreshed_ReturnSuccessMessage() {
        userFixture.createUserWithUsername("Alex");
        String refreshToken = generateValidRefreshToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "refreshToken=" + refreshToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/refresh-token", entity, ApiResponse.class);

        assertThat(response.getBody().message()).isEqualTo("Your session has been successfully extended.");
    }

    @Test
    void whenInvalidToken_StatusForbidden() {
        String invalidRefreshToken = "invalidToken";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "refreshToken=" + invalidRefreshToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/refresh-token", entity, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void whenMissingToken_StatusForbidden() {
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/refresh-token", null, ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    @Test
    void whenMissingToken_ReturnErrorMessage() {
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/refresh-token", null, ErrorResponse.class);

        assertThat(response.getBody().getError()).isEqualTo("Session renewal failed: please log in again.");
    }

    private String extractJwtTokenFromSetCookieHeader(String setCookieHeader) {
        String prefix = "jwtToken=";
        int startIndex = setCookieHeader.indexOf(prefix);
        if (startIndex == -1) {
            return "";
        }
        startIndex += prefix.length();
        int endIndex = setCookieHeader.indexOf(";", startIndex);
        if (endIndex == -1) {
            endIndex = setCookieHeader.length();
        }
        return setCookieHeader.substring(startIndex, endIndex);
    }
}
