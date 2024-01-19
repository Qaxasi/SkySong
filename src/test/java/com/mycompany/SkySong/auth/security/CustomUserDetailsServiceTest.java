package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.testsupport.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static com.mycompany.SkySong.testsupport.auth.security.CustomUserDetailsServiceTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomUserDetailsServiceTest extends BaseIT {
    @Autowired
    private CustomUserDetailsService detailsService;

    @Test
    void whenMissingUserByUsername_ThrowException() {
        assertThrows(UsernameNotFoundException.class,
                () -> detailsService.loadUserByUsername("mark"));
    }
    @Test
    void whenMissingUserByEmail_ThrowException() {

        //In our implementation, the email can serve as the username
        assertThrows(UsernameNotFoundException.class,
                () -> detailsService.loadUserByUsername("mark@mail.com"));
    }
    @Test
    void whenUserRegular_AdminRoleIsNotAssigned() {
        //when
        UserDetails userDetails = detailsService.loadUserByUsername("User");

        //then
        assertUserDoesNotHaveAuthorities(userDetails, "ROLE_ADMIN");
    }
    @Test
    void whenLoadedRegularUserByUsername_AssignsUserRole() {
        //when
        UserDetails userDetails = detailsService.loadUserByUsername("User");

        //then
        assertUserHasAuthorities(userDetails, "ROLE_USER");
    }
    @Test
    void whenLoadedRegularUserByEmail_AssignsUserRole() {
        //when
        UserDetails userDetails = detailsService.loadUserByUsername("mail@mail.com");

        //then
        assertUserHasAuthorities(userDetails, "ROLE_USER");
    }
    @Test
    void whenLoadedAdminUserByUsername_AssignsUserAndAdminRole() {
        UserDetails userDetails = detailsService.loadUserByUsername("testAdmin");

        assertUserHasAuthorities(userDetails, "ROLE_USER", "ROLE_ADMIN");
    }
    @Test
    void whenLoadedAdminUserByEmail_AssignsUserAndAdminRole() {

        //In our implementation, the email can serve as the username
        UserDetails userDetails = detailsService.loadUserByUsername("testAdmin@mail.com");

        assertUserHasAuthorities(userDetails, "ROLE_USER", "ROLE_ADMIN");
    }
}
