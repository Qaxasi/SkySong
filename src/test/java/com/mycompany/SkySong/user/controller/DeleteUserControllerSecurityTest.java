package com.mycompany.SkySong.user.controller;

import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.testsupport.controller.DeleteRequestAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

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
    void shouldReceiveOkStatusWhenDeletingUserWithValidId() throws Exception{
        long userId = 1L;

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 200);
    }
    @Test
    @WithAnonymousUser
    void shouldReturnUnauthorizedWhenDeletingUserWithoutBeingAuthenticated() throws Exception {
        long userId = 1L;

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 401);
    }
    @Test
    void shouldReturnStatusForbiddenWhenUserWithInsufficientPrivilegesTriesToDeleteUser() throws Exception {
        long userId = 1L;

        DeleteRequestAssertions.assertDeleteStatusReturns(
                mockMvc, "/api/v1/users/" + userId, 403);
    }
}
