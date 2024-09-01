package com.mycompany.SkySong.adapter.refreshToken.controller;

import com.mycompany.SkySong.adapter.exception.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RefreshTokenControllerTest  {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void whenInvalidToken_ReturnForbidden() {
        String invalidRefreshToken = "invalidToken";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "refreshToken=" + invalidRefreshToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/refresh-token", entity, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void whenMissingToken_ReturnForbidden() {
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
}
