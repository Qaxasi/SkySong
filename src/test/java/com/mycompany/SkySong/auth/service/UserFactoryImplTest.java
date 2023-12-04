package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.model.entity.Role;
import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.exception.InternalErrorException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserFactoryImplTest {
    @InjectMocks
    private UserFactoryImpl userFactory;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ApplicationMessageService messageService;

    @Test
    void shouldCreateUserWithGivenDetails() {
        String username = "testUsername";
        String email = "testEmail@gmail.com";
        String password = "testPassword@123";
        String encodedPassword = "encodedPassword";
        Role role = new Role(UserRole.ROLE_USER);

        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        User user = userFactory.createUser(registerRequest, role);

        assertEquals(user.getUsername(), registerRequest.username());
        assertEquals(user.getEmail(), registerRequest.email());
        assertEquals(user.getPassword(), encodedPassword);
        assertTrue(user.getRoles().contains(role));
    }
    @Test
    void shouldThrowExceptionWhenPasswordEncodingFailed() {
        String username = "testUsername";
        String email = "testEmail@gmail.com";
        String password = "testPassword@123";
        Role role = new Role(UserRole.ROLE_USER);

        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(passwordEncoder.encode(password)).thenThrow(new IllegalArgumentException("Test exception"));

        assertThrows(InternalErrorException.class, () -> userFactory.createUser(registerRequest, role));
    }
    @Test
    void shouldReturnMessageWhenPasswordEncodingFailed() {
        String username = "testUsername";
        String email = "testEmail@gmail.com";
        String password = "testPassword@123";
        Role role = new Role(UserRole.ROLE_USER);

        String expectedMessage = "Test message";

        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        when(passwordEncoder.encode(password)).thenThrow(new IllegalArgumentException("Test exception"));
        when(messageService.getMessage("password.encoding.error")).thenReturn(expectedMessage);

        Exception exception = assertThrows(InternalErrorException.class,
                () -> userFactory.createUser(registerRequest, role));

        assertEquals(expectedMessage, exception.getMessage());
    }
}
