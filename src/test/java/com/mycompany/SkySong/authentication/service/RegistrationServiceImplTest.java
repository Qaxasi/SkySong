package com.mycompany.SkySong.authentication.service;

import com.mycompany.SkySong.authentication.exception.RegisterException;
import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.model.dto.RegistrationResponse;
import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.repository.RoleDAO;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceImplTest {
    private RegistrationServiceImpl registrationService;
    @Mock
    private UserDAO userDAO;
    @Mock
    private RoleDAO roleDAO;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationServiceImpl(userDAO, roleDAO, passwordEncoder);
    }
    @Test
    void shouldSuccessfullyRegisterNewUserWithValidCredentials() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "testEmail@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername(registerRequest.username())).thenReturn(false);
        when(userDAO.existsByEmail(registerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("encodedPassword");
        when(roleDAO.findByName(any())).thenReturn(Optional.of(new Role()));

        RegistrationResponse response = registrationService.register(registerRequest);

        String expectedMessage = "User registered successfully";
        assertEquals(response.message(), expectedMessage);
    }
    @Test
    void shouldThrowExceptionWithCorrectMessageWhenTryRegisterWithExistUsername() {
        RegisterRequest registerRequest = new RegisterRequest(
                "existUsername", "testEmail@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername(registerRequest.username())).thenReturn(true);

        assertThatExceptionOfType(RegisterException.class)
                .isThrownBy(() -> registrationService.register(registerRequest))
                .withMessage("Username is already exist!.");
    }
    @Test
    void shouldThrowExceptionWithCorrectMessageWhenTryRegisterWithExistingEmail() {
        RegisterRequest registerRequest = new RegisterRequest(
                "testUsername", "existEmai@gmail.com", "testPassword@123");

        when(userDAO.existsByUsername(registerRequest.username())).thenReturn(false);
        when(userDAO.existsByEmail(registerRequest.email())).thenReturn(true);

        assertThatExceptionOfType(RegisterException.class)
                .isThrownBy(() -> registrationService.register(registerRequest))
                .withMessage("Email is already exist!.");
    }


}
