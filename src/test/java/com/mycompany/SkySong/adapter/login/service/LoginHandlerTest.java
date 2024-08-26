package com.mycompany.SkySong.adapter.login.service;

import com.mycompany.SkySong.adapter.login.dto.LoginResponse;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginHandlerTest extends BaseIT {

    @Autowired
    private LoginHandler login;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;
    private UserFixture userFixture;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);

        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenInvalidCredentials_ThrowException() {
        createUser("Alex", "Password#3");
        assertThrows(BadCredentialsException.class,
                () -> login("Invalid", "Invalid"));
    }

    @Test
    void whenLoginSuccess_SetSecurityContext() {
        createUser("Alex", "Password#3");
        login.login("Alex", "Password#3");
        assertNotNull(SecurityContextHolder.getContext());
    }

    @Test
    void whenLoginSuccess_ResponseContainsJwtToken() {
        createUser("Alex", "Password#3");
        LoginResponse response = login.login("Alex", "Password#3");
        assertNotNull(response.jwtToken());
    }

    @Test
    void whenLoginSuccess_ResponseContainsRefreshToken() {
        createUser("Alex", "Password#3");
        LoginResponse response = login.login("Alex", "Password#3");
        assertNotNull(response.refreshToken());
    }

    private void createUser(String username, String password) {
        userFixture.createUserWithUsernameAndPassword(username, password);
    }

    private void login(String usernameOrEmail, String password) {
        login.login(usernameOrEmail, password);
    }
}
