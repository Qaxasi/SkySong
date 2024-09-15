package com.mycompany.SkySong.adapter.registration.controller;

import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testutils.auth.UserBuilder;
import com.mycompany.SkySong.testutils.auth.UserFixture;
import com.mycompany.SkySong.testutils.common.BaseIT;
import com.mycompany.SkySong.testutils.data.RegistrationRequestData;
import com.mycompany.SkySong.testutils.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.mycompany.SkySong.testutils.utils.JsonUtils.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;

class RegistrationControllerTest extends BaseIT {

    @Autowired
    private TestRestTemplate restTemplate;
    private RegistrationRequestData requests;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    private UserFixture userFixture;

    @BeforeEach
    void setup() {
        requests = new RegistrationRequestData();

        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
    }

    @Test
    void whenUserWithGivenCredentialsExist_ReturnBadRequest() {
        userFixture.createUserWithUsername("Alex");

        ResponseEntity<Void> response = sendRequest("/api/v1/auth/register", requests.requestWithUsername("Alex"), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenRegistrationSuccess_StatusCreated() {
        ResponseEntity<Void> response = sendRequest("/api/v1/auth/register", requests.validCredentials, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void whenInvalidCredentials_ReturnBadRequest() {
        ResponseEntity<Void> response = sendRequest("/api/v1/auth/register", requests.emailInvalidFormat, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenMalformedRequest_ReturnBadRequest() {
        ResponseEntity<Void> response = sendMalformedRequest("/api/v1/auth/register", requests.malformedRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenEmptyCredentials_ReturnErrorMessages() {
        ResponseEntity<String> response = sendRequest("/api/v1/auth/register", requests.emptyCredentials, String.class);

        assertThat(response.getBody())
                .contains("\"username\":\"The username field cannot be empty\"")
                .contains("\"email\":\"The email field cannot be empty\"")
                .contains("\"password\":\"The password field cannot be empty\"");
    }

    private <T> ResponseEntity<T> sendRequest(String endpoint, Object requestBody, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(asJsonString(requestBody), headers);
        return restTemplate.postForEntity(endpoint, request, responseType);
    }

    private ResponseEntity<Void> sendMalformedRequest(String endpoint, String requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        return restTemplate.postForEntity(endpoint, request, Void.class);
    }
}
