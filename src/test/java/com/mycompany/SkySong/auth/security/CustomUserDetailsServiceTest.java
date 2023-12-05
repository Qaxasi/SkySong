package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
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
    void shouldThrowExceptionWhenLoadingMissingUserByUsername() {
        String nonExistentUsername = "nonExistentUsername";

        when(userDAO.findByUsername(nonExistentUsername)).thenThrow(new UsernameNotFoundException("User not found."));

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(nonExistentUsername));
    }
    @Test
    void shouldThrowExceptionWhenLoadingMissingUserByEmail() {
        String nonExistentEmail = "nonExistentEmail@gmail.com";

        when(userDAO.findByUsername(nonExistentEmail)).thenThrow(new UsernameNotFoundException("User not found"));

        //In our implementation, the email can serve as the username
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(nonExistentEmail));
    }
}
