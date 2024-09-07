package com.mycompany.SkySong.adapter.user.delete.controller;

import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.auth.common.UserIdFetcher;
import com.mycompany.SkySong.testsupport.common.AuthenticationTestHelper;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteUserControllerTest extends BaseIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private AuthenticationTestHelper auth;
    private UserFixture userFixture;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserIdFetcher userIdFetcher;

    @BeforeEach
    void setup() {
        auth = new AuthenticationTestHelper(restTemplate);

        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
    }

    @Test
    void whenUserIdNotExist_ReturnStatusNotFound() {
        int userId = 1000;

        createAdminUser();
        String jwtToken = auth.loginAdminUser();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwtToken=" + jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("/api/v1/users/" + userId, HttpMethod.DELETE, entity, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenNoUserId_ReturnBadRequest() {
      createAdminUser();
      String jwtToken = auth.loginAdminUser();

      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.COOKIE, "jwtToken=" + jwtToken);

      HttpEntity<Void> entity = new HttpEntity<>(headers);

      ResponseEntity<Void> response = restTemplate.exchange("/api/v1/users/", HttpMethod.DELETE, entity, Void.class);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenNoUserId_ReturnErrorMessage() {
        createAdminUser();
        String jwtToken = auth.loginAdminUser();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwtToken=" + jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/users/", HttpMethod.DELETE, entity, String.class);

        assertThat(response.getBody()).contains("\"error\":\"User ID is required and cannot be empty.\"");
    }

    @Test
    void whenInvalidUserIdFormat_ReturnErrorMessage() {
        createAdminUser();
        String jwtToken = auth.loginAdminUser();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwtToken=" + jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String userId = "invalid";
        ResponseEntity<String> response = restTemplate.exchange("/api/v1/users/" + userId, HttpMethod.DELETE, entity, String.class);

        assertThat(response.getBody()).contains("\"error\":\"Invalid input data format.\"");
    }

    @Test
    void whenUserIsAdmin_SuccessDeletionReturnStatusOk() {
        createUserWithUsername("Alex");

        int userId = userIdFetcher.fetchByUsername("Alex");

        createAdminUser();
        String jwtToken = auth.loginAdminUser();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwtToken=" + jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("/api/v1/users/" + userId, HttpMethod.DELETE, entity, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void whenUserDeleted_SecondDeletionReturnNotFound() {
        createUserWithUsername("Alex");

        int userId = userIdFetcher.fetchByUsername("Alex");

        createAdminUser();
        String jwtToken = auth.loginAdminUser();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwtToken=" + jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        restTemplate.exchange("/api/v1/users/" + userId, HttpMethod.DELETE, entity, Void.class);

        ResponseEntity<Void> response = restTemplate.exchange("/api/v1/users/" + userId, HttpMethod.DELETE, entity, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenUnauthenticatedUser_ReturnUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("/api/v1/users/", HttpMethod.DELETE, entity, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenUnauthenticatedUser_ReturnErrorMessage() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/users/", HttpMethod.DELETE, entity, String.class);
        assertThat(response.getBody()).contains("\"error\":\"Unauthorized access. Please log in.\"");
    }

    @Test
    void whenRegularUser_ReturnForbidden() {
        createUserWithUsername("Alex");

        int userId = userIdFetcher.fetchByUsername("Alex");

        createRegularUser();
        String jwtToken = auth.loginRegularUser();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwtToken=" + jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange("/api/v1/users/" + userId, HttpMethod.DELETE, entity, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void whenRegularUser_ReturnErrorMessage() {
        createUserWithUsername("Alex");

        int userId = userIdFetcher.fetchByUsername("Alex");

        createRegularUser();
        String jwtToken = auth.loginRegularUser();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwtToken=" + jwtToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/v1/users/" + userId, HttpMethod.DELETE, entity, String.class);

        assertThat(response.getBody()).contains("\"error\":\"You do not have permission to perform this operation.\"");
    }

    private void createAdminUser() {
        userFixture.createAdminUser();
    }

    private void createRegularUser() {
        userFixture.createRegularUser();
    }

    private void createUserWithUsername(String username) {
        userFixture.createUserWithUsername(username);
    }
}
