package com.mycompany.SkySong.adapter.refreshToken.controller;

import com.mycompany.SkySong.adapter.exception.response.ErrorResponse;
import com.mycompany.SkySong.testutils.auth.TestJwtTokenGenerator;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testutils.auth.UserBuilder;
import com.mycompany.SkySong.testutils.auth.UserFixture;
import com.mycompany.SkySong.testutils.common.BaseIT;
import com.mycompany.SkySong.testutils.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenControllerTest extends BaseIT {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    private UserFixture userFixture;
    private TestJwtTokenGenerator jwtGenerator;

    @BeforeEach
    void setup() {
        jwtGenerator = new TestJwtTokenGenerator();
        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
    }

    @Test
    void whenInvalidCookieProvided_ReturnStatusForbidden() {
        String refreshToken = generateValidRefreshToken();
        HttpHeaders headers = createHeadersWithCookie("invalidName", refreshToken);

        ResponseEntity<ErrorResponse> response = sendRequest("/api/v1/auth/refresh-token", headers, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void whenValidRefreshTokenProvided_SetJwtTokenInCookie() {
        String refreshToken = generateValidRefreshToken();
        HttpHeaders headers = createHeadersWithCookie("refreshToken=", refreshToken);

        ResponseEntity<ApiResponse> response = sendRequest("/api/v1/auth/refresh-token", headers, ApiResponse.class);

        String setCookieHeader = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertThat(setCookieHeader).isNotNull();
        String jwtToken = extractJwtTokenFromSetCookieHeader(setCookieHeader);
        assertThat(jwtToken).isNotEmpty();
    }

    @Test
    void whenValidRefreshTokenProvided_SetCookieWithCorrectAge() {
        String refreshToken = generateValidRefreshToken();
        HttpHeaders headers = createHeadersWithCookie("refreshToken=", refreshToken);

        ResponseEntity<ApiResponse> response = sendRequest("/api/v1/auth/refresh-token", headers, ApiResponse.class);

        assertThat(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE))
                .isNotNull()
                .contains("Max-Age=600");
    }

    @Test
    void whenValidRefreshTokenProvided_SetCookieWithCorrectPath() {
        String refreshToken = generateValidRefreshToken();
        HttpHeaders headers = createHeadersWithCookie("refreshToken=", refreshToken);

        ResponseEntity<ApiResponse> response = sendRequest("/api/v1/auth/refresh-token", headers, ApiResponse.class);

        assertThat(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE))
                .isNotNull()
                .contains("Path=/api");
    }

    @Test
    void whenRefreshingTokenWithValidToken_StatusOk() {
        String refreshToken = generateValidRefreshToken();
        HttpHeaders headers = createHeadersWithCookie("refreshToken=", refreshToken);

        ResponseEntity<ApiResponse> response = sendRequest("/api/v1/auth/refresh-token", headers, ApiResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void whenTokenRefreshed_ReturnSuccessMessage() {
        String refreshToken = generateValidRefreshToken();
        HttpHeaders headers = createHeadersWithCookie("refreshToken=", refreshToken);

        ResponseEntity<ApiResponse> response = sendRequest("/api/v1/auth/refresh-token", headers, ApiResponse.class);

        assertThat(response.getBody().message()).isEqualTo("Your session has been successfully extended.");
    }

    @Test
    void whenInvalidToken_StatusForbidden() {
        HttpHeaders headers = createHeadersWithCookie("refreshToken=", "invalidTokenValue");

        ResponseEntity<ErrorResponse> response = sendRequest("/api/v1/auth/refresh-token", headers, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void whenMissingToken_StatusForbidden() {
        ResponseEntity<ErrorResponse> response = sendRequest("/api/v1/auth/refresh-token", null, ErrorResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    @Test
    void whenMissingToken_ReturnErrorMessage() {
        ResponseEntity<ErrorResponse> response = sendRequest("/api/v1/auth/refresh-token", null, ErrorResponse.class);

        assertThat(response.getBody().getError()).isEqualTo("Session renewal failed: please log in again.");
    }

    private String generateValidRefreshToken() {
        userFixture.createUserWithUsername("Alex");
        return jwtGenerator.generateValidRefreshToken("Alex");
    }

    private <T> ResponseEntity<T> sendRequest(String endpoint, HttpHeaders headers, Class<T> responseType) {
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.postForEntity(endpoint, entity, responseType);
    }

    private HttpHeaders createHeadersWithCookie(String name, String value) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, name + value);;
        return headers;
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
