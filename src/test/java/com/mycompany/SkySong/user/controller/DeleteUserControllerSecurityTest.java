package com.mycompany.SkySong.user.controller;

import com.jayway.jsonpath.JsonPath;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.testsupport.controller.DeleteRequestAssertions;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @BeforeEach
    void init() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/test-data-setup.sql"));
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

       String jwtToken = loginAndGetToken(requestBody);

       Cookie cookie = new Cookie("auth_token", jwtToken);

       DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, cookie, 200);
    }
    @Test
    @WithAnonymousUser
    void shouldReturnUnauthorizedWhenDeletingUserWithoutBeingAuthenticated() throws Exception {
        final long userId = 1L;

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, null, 401);
    }
    @Test
    void shouldReturnStatusForbiddenWhenUserWithInsufficientPrivilegesTriesToDeleteUser() throws Exception {
        long userId = 1L;

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 403);
    }

    private String loginAndGetToken(String requestBody) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        return JsonPath.parse(responseString).read("$.accessToken");
    }
}
