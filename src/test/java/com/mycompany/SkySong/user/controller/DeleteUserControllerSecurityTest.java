package com.mycompany.SkySong.user.controller;

import com.jayway.jsonpath.JsonPath;
import com.mycompany.SkySong.testsupport.AuthenticationTestHelper;
import com.mycompany.SkySong.testsupport.DatabaseTestHelper;
import com.mycompany.SkySong.testsupport.controller.DeleteRequestAssertions;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteUserControllerSecurityTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DatabaseTestHelper databaseTestHelper;
    @Autowired
    private AuthenticationTestHelper authenticationTestHelper;
    private Long userId;

    @BeforeEach
    void init() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/test-data-setup.sql"));

            final String usernameToDelete = "testSecondUsername";
            this.userId = databaseTestHelper.getUserIdByUsername(dataSource, usernameToDelete);
        }
    }
    @AfterEach
    void cleanup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
    }
    @Test
    void shouldReceiveOkStatusWhenDeletingUserWithValidId() throws Exception {
        final String requestBody = "{\"usernameOrEmail\": \"testAdmin\",\"password\": \"testPassword@123\"}";
        final long userId = 1L;

       String jwtToken = authenticationTestHelper.loginAndGetToken(mockMvc, requestBody);

       Cookie cookie = new Cookie("auth_token", jwtToken);

       DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, cookie, 200);
    }
    @Test
    @WithAnonymousUser
    void shouldReturnUnauthorizedForUnauthenticatedUser() throws Exception {
        final long userId = 1L;

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, null, 401);
    }
    @Test
    @WithAnonymousUser
    void shouldReturnUnauthorizedMessageForUnauthenticatedUser() throws Exception {
        final long userId = 1L;
        final String expectedMessage = "Unauthorized access. Please log in.";

        DeleteRequestAssertions.assertDeleteResponse(mockMvc, "/api/v1/users/" + userId,
                null, expectedMessage);
    }
    @Test
    void shouldReturnStatusForbiddenWhenUserWithInsufficientPrivilegesTriesToDeleteUser() throws Exception {
        final String requestBody = "{\"usernameOrEmail\": \"testUsername\",\"password\": \"testPassword@123\"}";
        final long userId = 1L;

        String jwtToken = authenticationTestHelper.loginAndGetToken(mockMvc, requestBody);

        Cookie cookie = new Cookie("auth_token", jwtToken);

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, cookie, 403);
    }
    @Test
    void shouldReturnForbiddenMessageWhenUserWithInsufficientPrivilegesTriesToDeleteUser() throws Exception {
        final String requestBody = "{\"usernameOrEmail\": \"testUsername\",\"password\": \"testPassword@123\"}";
        final long userId = 1L;
        final String expectedMessage = "You do not have permission to perform this operation.";

        String jwtToken = authenticationTestHelper.loginAndGetToken(mockMvc, requestBody);

        Cookie cookie = new Cookie("auth_token", jwtToken);

        DeleteRequestAssertions.assertDeleteResponse(mockMvc, "/api/v1/users/" + userId,
                cookie, expectedMessage);
    }
}
