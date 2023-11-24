package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.auth.security.CustomUserDetailsService;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private UserDAO userDAO;
    @Mock
    private ApplicationMessageService messageService;
    @Test
    void shouldReturnExceptionMessageWhenUserWithUsernameNotFound() {
        String username = "nonExistingUser";
        String expectedMessage = "User not found with username or email: " + username;

        when(userDAO.findByUsername(username)).thenReturn(Optional.empty());
        when(messageService.getMessage("user.not.found", username)).thenReturn(expectedMessage);

        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(username));

        assertEquals(expectedMessage, exception.getMessage());
    }
}
