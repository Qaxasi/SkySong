package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.entity.User;
import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.mycompany.SkySong.testsupport.auth.security.CustomUserDetailsServiceTestHelper.*;
import static com.mycompany.SkySong.testsupport.common.UserTestConfigurator.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private UserDAO userDAO;
    @Mock
    private ApplicationMessageService messageService;
    @Test
    void whenMissingUserByUsername_ThrowException() {
        setupNonExistentUserByUsername(userDAO, "mark");

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("mark"));
    }
    @Test
    void whenMissingUserByEmail_ThrowException() {
        setupNonExistentUserByEmail(userDAO, "mark@mail.com");

        //In our implementation, the email can serve as the username
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("mark@mail.com"));
    }
    @Test
    void whenLoadedRegularUserByUsername_AssignsUserRole() {
        UserDetails userDetails = setupAndLoadRegularUserByUsername(userDAO, customUserDetailsService);

        assertUserHasAuthorities(userDetails, "ROLE_USER");
    }
    @Test
    void whenLoadedRegularUserByEmail_AssignsUserRole() {
        UserDetails userDetails = setupAndLoadRegularUserByEmail(userDAO, customUserDetailsService);

        assertUserHasAuthorities(userDetails, "ROLE_USER");
    }
    @Test
    void whenLoadedAdminUserByUsername_AssignsUserAndAdminRole() {
        UserDetails userDetails = setupAndLoadAdminUserByUsername(userDAO, customUserDetailsService);

        assertUserHasAuthorities(userDetails, "ROLE_USER", "ROLE_ADMIN");
    }
}
