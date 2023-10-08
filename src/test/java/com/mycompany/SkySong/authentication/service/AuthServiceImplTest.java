package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.dto.LoginRequest;
import com.mycompany.SkySong.authentication.role.repository.RoleDAO;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.service.impl.AuthServiceImpl;
import com.mycompany.SkySong.authentication.user.entity.User;
import com.mycompany.SkySong.authentication.user.repository.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    private AuthServiceImpl authService;
    @Mock
    private UserDAO userDAO;
    @Mock
    private RoleDAO roleDAO;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        authService = new AuthServiceImpl(authenticationManager, userDAO, roleDAO, passwordEncoder, jwtTokenProvider);
    }
    @Test
    void shouldReturnValidTokenWhenGivenValidCredentials() {
        User validUser = new User();
        validUser.setUsername("testUsername");
        validUser.setPassword("testPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("validToken");

        String token = authService.login(new LoginRequest("testUsername", "testPassword"));

        assertEquals("validToken", token);
    }

    @Test
    void shouldThrowExceptionWhenLoggingWithInvalidPassword() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Niepoprawne dane uwierzytelniające"));

        assertThrows(BadCredentialsException.class, () ->
                authService.login(new LoginRequest("testUsername", "wrongPassword")));

    }

    @Test
    void shouldThrowExceptionWhenLoggingWithInvalidUsernameOrEmail() {
        User user = new User();
        user.setEmail("testEmail@gmail.com");
        user.setPassword("testPassword");

        when(authenticationManager.authenticate(any())).thenThrow(
                new BadCredentialsException("Incorrect credentials"));

        assertThrows(BadCredentialsException.class, () ->
                authService.login(new LoginRequest("wrongEmail@gmail.com", "testPassword")));
    }


}
