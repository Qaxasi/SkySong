package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.dto.LoginRequest;
import com.mycompany.SkySong.authentication.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.dto.RegistrationResponse;
import com.mycompany.SkySong.authentication.exception.RegisterException;
import com.mycompany.SkySong.authentication.role.entity.Role;
import com.mycompany.SkySong.authentication.role.entity.UserRole;
import com.mycompany.SkySong.authentication.role.repository.RoleDAO;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.service.impl.AuthServiceImpl;
import com.mycompany.SkySong.authentication.user.entity.User;
import com.mycompany.SkySong.authentication.user.repository.UserDAO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Validator;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private Validator validator;

    @BeforeEach
    public void setUp() {
        authService = new AuthServiceImpl(authenticationManager, userDAO, roleDAO, passwordEncoder, jwtTokenProvider);
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    @Test
    void shouldReturnValidTokenWhenGivenValidCredentials() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testPassword@123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("validToken");

        String token = authService.login(loginRequest);

        assertEquals("validToken", token);
    }

    @Test
    void shouldThrowExceptionWhenLoggingWithInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest("testUsername", "testWrongPassword@123");

        when(authenticationManager.authenticate(any())).thenThrow(
                new BadCredentialsException("Incorrect credentials"));

        assertThrows(BadCredentialsException.class, () ->
                authService.login(loginRequest));

    }

    @Test
    void shouldThrowExceptionWhenLoggingWithInvalidUsername() {
        User user = new User();
        user.setEmail("testEmail@gmail.com");
        user.setPassword("testPassword@123");

        when(authenticationManager.authenticate(any())).thenThrow(
                new BadCredentialsException("Incorrect credentials"));

        assertThrows(BadCredentialsException.class, () ->
                authService.login(new LoginRequest("wrongEmail@gmail.com", "testPassword@123")));
    }

    @Test
    void shouldReturnSuccessResponseWhenRegisterNewUserWithUniqueData() {
        RegisterRequest request = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername(request.username())).thenReturn(false);
        when(userDAO.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("hashedPassword");
        when(roleDAO.findByName(UserRole.ROLE_USER)).thenReturn(Optional.of(new Role(UserRole.ROLE_USER)));

        RegistrationResponse response = authService.register(request);

        assertTrue(response.success());
        assertEquals("User registered successfully", response.message());
        verify(userDAO, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenNewUserTryRegisterWithExistingUsername() {
        RegisterRequest request = new RegisterRequest(
                "testExistingUsername", "testEmail@gmail.com", "testPassword");

        when(userDAO.existsByUsername(request.username())).thenReturn(true);

        assertThrows(RegisterException.class, () -> authService.register(request));
        verify(userDAO, times(0)).save(any(User.class));

    }
    @Test
    void shouldThrowExceptionWhenNewUserTryRegisterWithExistingEmail() {
        RegisterRequest request = new RegisterRequest(
                "testUsername", "testExistingEmail@gmail.com", "testPassword");

        when(userDAO.existsByUsername(request.username())).thenReturn(false);
        when(userDAO.existsByEmail(request.email())).thenReturn(true);

        assertThrows(RegisterException.class, () -> authService.register(request));
        verify(userDAO, times(0)).save(any(User.class));
    }
    @Test
    void shouldDetectInvalidUsernameFormatWhileValidatingRegistrationRequest() {
        RegisterRequest request = new RegisterRequest(
                "invalid%Username", "testEmail@gmail.com", "testPassword@123");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(violation ->
                "username".equals(violation.getPropertyPath().toString())));
        assertFalse(violations.stream().anyMatch(violation ->
                "email".equals(violation.getPropertyPath().toString())));
        assertFalse(violations.stream().anyMatch(violation ->
                "password".equals(violation.getPropertyPath().toString())));

    }

    @Test
    void shouldDetectInvalidEmailFormatWhileValidatingRegistrationRequest() {
        RegisterRequest request = new RegisterRequest(
                "testUsername", "testInvalidEmail", "testPassword@123");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertFalse(violations.stream().anyMatch(violation ->
                "username".equals(violation.getPropertyPath().toString())));
        assertTrue(violations.stream().anyMatch(violation ->
                "email".equals(violation.getPropertyPath().toString())));
        assertFalse(violations.stream().anyMatch(violation ->
                "password".equals(violation.getPropertyPath().toString())));
    }

    @Test
    void shouldDetectInvalidPasswordFormatWhileValidatingRegistrationRequest() {
        RegisterRequest request = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testInvalidPassword");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertFalse(violations.stream().anyMatch(violation ->
                "username".equals(violation.getPropertyPath().toString())));
        assertFalse(violations.stream().anyMatch(violation ->
                "email".equals(violation.getPropertyPath().toString())));
        assertTrue(violations.stream().anyMatch(violation ->
                "password".equals(violation.getPropertyPath().toString())));

    }



}
