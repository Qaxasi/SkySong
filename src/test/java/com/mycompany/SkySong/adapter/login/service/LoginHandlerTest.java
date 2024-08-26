package com.mycompany.SkySong.adapter.login.service;

import com.mycompany.SkySong.infrastructure.dao.InMemoryRoleDAO;
import com.mycompany.SkySong.infrastructure.dao.InMemoryUserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class LoginHandlerTest {

    @Autowired
    private LoginHandler login;
    private UserFixture userFixture;
    private InMemoryRoleDAO roleDAO;
    private InMemoryUserDAO userDAO;

    @BeforeEach
    void setUp() {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        roleDAO = new InMemoryRoleDAO();
        userDAO = new InMemoryUserDAO(roleDAO);
        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
    }

    @AfterEach
    void cleanUp() {
        roleDAO.clear();
        userDAO.clear();
    }

    @Test
    void whenInvalidCredentials_ThrowException() {
        createUser("Alex", "Password#3");
        assertThrows(BadCredentialsException.class,
                () -> login("Invalid", "Invalid"));
    }

    private void createUser(String username, String password) {
        userFixture.createUserWithUsernameAndPassword(username, password);
    }

    private void login(String usernameOrEmail, String password) {
        login.login(usernameOrEmail, password);
    }
}
