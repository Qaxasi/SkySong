package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.security.JwtTokenProvider;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    @InjectMocks
    private LoginServiceImpl loginService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;
    @Mock
    private ApplicationMessageService messageService;
    @Test
    void shouldReturnTokenOnSuccessfulLogin() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword@123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("validToken");

        String token = loginService.login(loginRequest);

        assertEquals("validToken", token);
    }
    @Test
    void shouldReturnErrorMessageAfterLoginWithInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("testWrongUsername", "testPassword@123");

        when(authenticationManager.authenticate(any())).thenThrow(
                new BadCredentialsException("Incorrect credentials"));

        when(messageService.getMessage("login.failure"))
                .thenReturn("Incorrect username/email or password.");

        Exception exception = assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));

        String expectedMessage = "Incorrect username/email or password.";
        assertEquals(expectedMessage, exception.getMessage());
    }
    @Test
    void shouldNotGenerateJwtTokenOnInvalidLogin() {
        LoginRequest loginRequest = new LoginRequest(
                "testWrongUsername", "testWrongPassword@123"        );

        when(authenticationManager.authenticate(any())).thenThrow(
                new BadCredentialsException("Incorrect credentials"));

        assertThrows(BadCredentialsException.class, () -> loginService.login(loginRequest));

        verify(jwtTokenProvider, never()).generateToken(authentication);
    }
    @Test
    void shouldPassCorrectCredentialsToAuthenticationManager() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword@123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        loginService.login(loginRequest);

        verify(authenticationManager).authenticate(
                eq(new UsernamePasswordAuthenticationToken("testUsername", "testPassword@123"))
        );
    }
}
