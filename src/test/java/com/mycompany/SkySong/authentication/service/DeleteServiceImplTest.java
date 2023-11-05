package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.exception.UserNotFoundException;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.impl.DeleteServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DeleteServiceImplTest {
    @Autowired
    private DeleteServiceImpl deleteService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDAO userDAO;
    @BeforeEach
    void init() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/user-data.sql"));
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/role-data.sql"));
        }
    }
    @AfterEach
    void cleanup() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
    }
    @Test
    void shouldDeleteUserWhenUserIdExists() {
        long userId = 1L;

        Optional<User> userBeforeDeletion = userDAO.findById(userId);
        assertTrue(userBeforeDeletion.isPresent());

        deleteService.deleteUser(userId);

        Optional<User> userAfterDeletion = userDAO.findById(userId);
        assertFalse(userAfterDeletion.isPresent());
    }
    @Test
    void shouldThrowUserNotFoundExceptionWhenUserIdDoesNotExist() {
        long notExistUserId = 10L;

        Optional<User> user = userDAO.findById(notExistUserId);
        assertFalse(user.isPresent());

        assertThrows(UserNotFoundException.class, () -> deleteService.deleteUser(notExistUserId));
    }
}
