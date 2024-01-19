package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.testsupport.BaseIT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static com.mycompany.SkySong.testsupport.auth.security.CustomUserDetailsServiceTestHelper.*;
import static com.mycompany.SkySong.testsupport.common.UserTestConfigurator.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomUserDetailsServiceTest extends BaseIT {
    @Autowired
    private CustomUserDetailsService detailsService;

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
    void whenUserRegular_AdminRoleIsNotAssigned() {
        UserDetails userDetails = setupAndLoadRegularUserByUsername(userDAO, customUserDetailsService);

        assertUserDoesNotHaveAuthorities(userDetails, "ROLE_ADMIN");
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
    @Test
    void whenLoadedAdminUserByEmail_AssignsUserAndAdminRole() {
        UserDetails userDetails = setupAndLoadAdminUserByEmail(userDAO, customUserDetailsService);

        assertUserHasAuthorities(userDetails, "ROLE_USER", "ROLE_ADMIN");
    }
}
