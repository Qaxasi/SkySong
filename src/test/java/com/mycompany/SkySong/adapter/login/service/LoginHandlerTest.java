package com.mycompany.SkySong.adapter.login.service;

import com.mycompany.SkySong.adapter.login.dto.LoginDto;
import com.mycompany.SkySong.adapter.login.dto.LoginResponse;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
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

    @BeforeEach
    void setup() {
        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
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
        login("Alex", "Password#3");
        assertNotNull(SecurityContextHolder.getContext());
    }

    @Test
    void whenLoginSuccess_ResponseContainsJwtToken() {
        createUser("Alex", "Password#3");
        LoginResponse response = login("Alex", "Password#3");
        assertNotNull(response.jwtToken());
    }

    @Test
    void whenLoginSuccess_ResponseContainsRefreshToken() {
        createUser("Alex", "Password#3");
        LoginResponse response = login("Alex", "Password#3");
        assertNotNull(response.refreshToken());
    }

    private void createUser(String username, String password) {
        userFixture.createUserWithUsernameAndPassword(username, password);
    }

    private LoginResponse login(String usernameOrEmail, String password) {
        return login.login(new LoginDto(usernameOrEmail, password));
    }
}
