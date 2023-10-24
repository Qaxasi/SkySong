package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.model.dto.LoginRequest;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.service.impl.LoginServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceImplTest {
    private LoginServiceImpl loginService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        loginService = new LoginServiceImpl(authenticationManager, jwtTokenProvider);

    }
    @Test
    void shouldLoginSuccessfullyWhenLoginWithValidCredentials() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword@123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("validToken");

        String token = loginService.login(loginRequest);

        assertEquals("validToken", token);
    }
    @Test
    void shouldThrowExceptionWhenLoggingWithInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testWrongPassword@123");

        when(authenticationManager.authenticate(any())).thenThrow(
                new BadCredentialsException("Incorrect credentials"));

        assertThrows(BadCredentialsException.class, () ->
                loginService.login(loginRequest));

    }

    @Test
    void shouldInvokeGenerateTokenWithProperAuthenticationAfterSuccessfulLogin() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword@123");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        loginService.login(loginRequest);

        verify(jwtTokenProvider).generateToken(authentication);
    }
}
