package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.auth.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private UserDAO userDAO;
    @BeforeEach
    void setUp() {
        customUserDetailsService = new CustomUserDetailsService(userDAO);
    }
    @Test
    void shouldReturnExceptionMessageWhenUserWithUsernameNotFound() {
        String username = "nonExistingUser";

        when(userDAO.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(username));

        String expectedMessage = "User not found with username or email: " + username;

        assertEquals(expectedMessage, exception.getMessage());
    }

}
