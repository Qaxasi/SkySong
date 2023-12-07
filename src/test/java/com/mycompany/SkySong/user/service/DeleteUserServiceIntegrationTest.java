package com.mycompany.SkySong.user.service;

import com.mycompany.SkySong.shared.exception.UserNotFoundException;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.repository.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class DeleteUserServiceIntegrationTest {
    @Autowired
    private DeleteUserService deleteService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TransactionTemplate transactionTemplate;
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
    @Test
    void shouldRollbackTransactionWhenErrorOccursAfterDelete() {
        Role role = new Role(UserRole.ROLE_USER);
        Set<Role> roles = Set.of(role);
        User user = new User(2,"testUniqueUsername", "testUniqueEmail@gmail.com",
                "testPassword@123", roles);

        User savedUser = userDAO.save(user);
        long userId = savedUser.getId();

        assertTrue(userDAO.existsById(userId));

        assertThrows(DataIntegrityViolationException.class, () -> {
            transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            transactionTemplate.execute(status -> {
                deleteService.deleteUser(userId);
                throw new DataIntegrityViolationException("Forced failure after delete operation");
            });
        });

        assertTrue(userDAO.existsById(userId));
    }
}
