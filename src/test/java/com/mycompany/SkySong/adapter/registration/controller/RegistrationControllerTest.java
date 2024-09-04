package com.mycompany.SkySong.adapter.registration.controller;

import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.mycompany.SkySong.testsupport.common.JsonUtils.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;

class RegistrationControllerTest extends BaseIT {

    @Autowired
    private TestRestTemplate restTemplate;
    private RegistrationRequests requests;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    private UserFixture userFixture;

    @BeforeEach
    void setup() {
        requests = new RegistrationRequests();

        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
    }

    @Test
    void whenRegistrationSuccess_StatusCreated() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(asJsonString(requests.validCredentials), headers);

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/v1/auth/register", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void whenInvalidCredentials_ReturnBadRequest() {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(asJsonString(requests.emailInvalidFormat), headers);

            ResponseEntity<Void> response = restTemplate.postForEntity("/api/v1/auth/register", request, Void.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenMalformedRequest_ReturnBadRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requests.malformedRequest, headers);

        ResponseEntity<Void> response = restTemplate.postForEntity("/api/v1/auth/register", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenEmptyCredentials_ReturnErrorMessages() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(asJsonString(requests.emptyCredentials), headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/auth/register", request, String.class);

        assertThat(response.getBody())
                .contains("\"username\":\"The username field cannot be empty\"")
                .contains("\"email\":\"The email field cannot be empty\"")
                .contains("\"password\":\"The password field cannot be empty\"");
    }
}
