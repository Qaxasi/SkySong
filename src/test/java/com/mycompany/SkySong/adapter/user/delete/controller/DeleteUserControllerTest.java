package com.mycompany.SkySong.adapter.user.delete.controller;

import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testutils.auth.UserBuilder;
import com.mycompany.SkySong.testutils.auth.UserFixture;
import com.mycompany.SkySong.testutils.auth.UserIdFetcher;
import com.mycompany.SkySong.testutils.auth.AuthenticationTestHelper;
import com.mycompany.SkySong.testutils.common.BaseIT;
import com.mycompany.SkySong.testutils.utils.CustomPasswordEncoder;
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

        String jwtToken = loginAsAdmin();
        HttpHeaders headers = createHeadersWithJwtToken(jwtToken);

        ResponseEntity<Void> response = sendDeleteRequest("/api/v1/users/" + userId, headers, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenNoUserId_ReturnBadRequest() {
      String jwtToken = loginAsAdmin();
      HttpHeaders headers = createHeadersWithJwtToken(jwtToken);

      ResponseEntity<Void> response = sendDeleteRequest("/api/v1/users/", headers, Void.class);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void whenNoUserId_ReturnErrorMessage() {
        String jwtToken = loginAsAdmin();
        HttpHeaders headers = createHeadersWithJwtToken(jwtToken);

        ResponseEntity<String> response = sendDeleteRequest("/api/v1/users/", headers, String.class);

        assertThat(response.getBody()).contains("\"error\":\"User ID is required and cannot be empty.\"");
    }

    @Test
    void whenInvalidUserIdFormat_ReturnErrorMessage() {
        String jwtToken = loginAsAdmin();
        HttpHeaders headers = createHeadersWithJwtToken(jwtToken);

        String userId = "invalid";
        ResponseEntity<String> response = sendDeleteRequest("/api/v1/users/" + userId, headers, String.class);

        assertThat(response.getBody()).contains("\"error\":\"Invalid input data format.\"");
    }

    @Test
    void whenUserIsAdmin_SuccessDeletionReturnStatusOk() {
        int userId = createUserAndFetchHisId("Alex");

        String jwtToken = loginAsAdmin();
        HttpHeaders headers = createHeadersWithJwtToken(jwtToken);

        ResponseEntity<Void> response = sendDeleteRequest("/api/v1/users/" + userId, headers, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void whenUserDeleted_SecondDeletionReturnNotFound() {
        int userId = createUserAndFetchHisId("Alex");

        String jwtToken = loginAsAdmin();
        HttpHeaders headers = createHeadersWithJwtToken(jwtToken);

        sendDeleteRequest("/api/v1/users/" + userId, headers, Void.class);

        ResponseEntity<Void> response = sendDeleteRequest("/api/v1/users/" + userId, headers, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void whenUnauthenticatedUser_ReturnUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Void> response = sendDeleteRequest("/api/v1/users/", headers, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void whenUnauthenticatedUser_ReturnErrorMessage() {
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> response = sendDeleteRequest("/api/v1/users/", headers, String.class);
        assertThat(response.getBody()).contains("\"error\":\"Unauthorized access. Please log in.\"");
    }

    @Test
    void whenRegularUser_ReturnForbidden() {
        int userId = createUserAndFetchHisId("Alex");

        String jwtToken = loginAsRegularUser();
        HttpHeaders headers = createHeadersWithJwtToken(jwtToken);

        ResponseEntity<Void> response = sendDeleteRequest("/api/v1/users/" + userId, headers, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void whenRegularUser_ReturnErrorMessage() {
        int userId = createUserAndFetchHisId("Alex");

        String jwtToken = loginAsRegularUser();
        HttpHeaders headers = createHeadersWithJwtToken(jwtToken);

        ResponseEntity<String> response = sendDeleteRequest("/api/v1/users/" + userId, headers, String.class);

        assertThat(response.getBody()).contains("\"error\":\"You do not have permission to perform this operation.\"");
    }

    private int createUserAndFetchHisId(String username) {
        userFixture.createUserWithUsername(username);
        return userIdFetcher.fetchByUsername(username);
    }

    private HttpHeaders createHeadersWithJwtToken(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, "jwtToken=" + jwtToken);
        return headers;
    }

    private <T> ResponseEntity<T> sendDeleteRequest(String endpoint, HttpHeaders headers, Class<T> responseType) {
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(endpoint, HttpMethod.DELETE, entity, responseType);
    }

    private String loginAsAdmin() {
        userFixture.createAdminUser();
        return auth.loginAdminUser();
    }

    private String loginAsRegularUser() {
        userFixture.createRegularUser();
        return auth.loginRegularUser();
    }
}
