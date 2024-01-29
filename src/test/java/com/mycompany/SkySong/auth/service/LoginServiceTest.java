package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.testsupport.BaseIT;
import com.mycompany.SkySong.testsupport.auth.LoginRequests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.mycompany.SkySong.ExceptionAssertionUtils.assertException;
import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest extends BaseIT {

    @Autowired
    private LoginService login;

    @Test
    void whenLoginSuccess_ReturnToken() {
        // given
        LoginRequest request = LoginRequests.VALID_CREDENTIALS;

        // when
        String token = login.login(request);

        // then
        assertNotNull(token);
    }
    @Test
    void whenInvalidPassword_ThrowException() {
        // given
        LoginRequest request = LoginRequests.INVALID_PASSWORD;

        // when & then
        assertException(() -> login.login(request), BadCredentialsException.class,
                "Incorrect username/email or password.");
    }
    @Test
    void whenInvalidEmail_ThrowException() {
        // given
        LoginRequest request = LoginRequests.INVALID_EMAIL;

        // when & then
        assertException(() -> login.login(request), BadCredentialsException.class,
                "Incorrect username/email or password.");
    }
    @Test
    void whenInvalidUsername_ThrowException() {
        // given
        LoginRequest request = LoginRequests.INVALID_USERNAME;

        // when & then
        assertException(() -> login.login(request), BadCredentialsException.class,
                "Incorrect username/email or password.");
    }
    @Test
    void whenLoginFailure_NotSetAuthContext() {
        // given
        LoginRequest request = LoginRequests.INVALID_PASSWORD;

        // when
        assertThrows(BadCredentialsException.class, () -> login.login(request));

        // then
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void whenLoginSuccess_SetAuthContext() {
        // given
        LoginRequest request = LoginRequests.VALID_CREDENTIALS;

        // when
        login.login(request);

        // then
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
    @Test
    void whenLoginSuccess_ReturnCorrectUsernameInAuth() {
        // given
        LoginRequest request = LoginRequests.VALID_CREDENTIALS;

        // when
        login.login(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // then
        assertEquals("User", authentication.getName());
    }
}
