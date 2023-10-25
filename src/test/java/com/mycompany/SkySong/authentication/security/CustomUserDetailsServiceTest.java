package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.model.entity.UserRole;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.secutiry.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
    void shouldReturnCorrectUserDetailsWhenLoggingWithUsername() {
        Set<Role> roles = Set.of(new Role(UserRole.ROLE_USER));
        User testUser = new User("testUsername", "testEmail@gmail.com", "testPassword@123", roles);

        when(userDAO.findByUsername("testUsername")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUsername");

        assertEquals("testUsername", userDetails.getUsername());
        assertEquals("testPassword@123", userDetails.getPassword());
    }
    @Test
    void shouldReturnCorrectUserDetailsWhenLoggingWithEmail() {
        Set<Role> roles = Set.of(new Role(UserRole.ROLE_USER));
        User testUser = new User("testUsername", "testEmail@gmail.com", "testPassword@123", roles);

        when(userDAO.findByEmail("testEmail@gmail.com")).thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testEmail@gmail.com");

        //In our implementation, the email can serve as the username

        assertEquals("testEmail@gmail.com", userDetails.getUsername());
        assertEquals("testPassword@123", userDetails.getPassword());
    }

}
