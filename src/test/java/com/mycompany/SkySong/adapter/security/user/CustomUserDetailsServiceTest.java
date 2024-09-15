package com.mycompany.SkySong.adapter.security.user;

import com.mycompany.SkySong.infrastructure.dao.InMemoryRoleDAO;
import com.mycompany.SkySong.infrastructure.dao.InMemoryUserDAO;
import com.mycompany.SkySong.testutils.auth.UserBuilder;
import com.mycompany.SkySong.testutils.auth.UserFixture;
import com.mycompany.SkySong.testutils.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsServiceTest {
    private InMemoryUserDAO userDAO;
    private InMemoryRoleDAO roleDAO;
    private CustomUserDetailsService detailsService;
    private UserFixture userFixture;

    @BeforeEach
    void setUp() {
        roleDAO = new InMemoryRoleDAO();
        userDAO = new InMemoryUserDAO(roleDAO);

        detailsService = new CustomUserDetailsService(userDAO, roleDAO);

        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
    }

    @AfterEach
    void clean() {
        roleDAO.clear();
        userDAO.clear();
    }

    @Test
    void whenLoadNonExistentUserByUsername_ThrowException() {
        assertThrows(UsernameNotFoundException.class,
                () -> loadUserByUsername("Max"));
    }

    @Test
    void whenLoadNonExistentUserByEmail_ThrowException() {
        //In our implementation, the email can serve as the username
        assertThrows(UsernameNotFoundException.class,
                () -> loadUserByUsername("max@mail.mail"));
    }

    @Test
    void whenLoadedRegularUser_AdminRoleIsNotGranted() {
        createRegularUserWithUsername("Alex");
        CustomUserDetails userDetails = loadUserByUsername("Alex");
        assertUserDoesNotHaveAuthorities(userDetails, "ROLE_ADMIN");
    }

    @Test
    void whenLoadedRegularUserByUsername_RegularRoleHasGranted() {
        createRegularUserWithUsername("Alex");
        CustomUserDetails userDetails = loadUserByUsername("Alex");
        assertUserHasAuthorities(userDetails, "ROLE_USER");
    }

    @Test
    void whenLoadedRegularUserByEmail_RegularRoleHasGranted() {
        createRegularUserWithEmail("mail@mail.com");
        CustomUserDetails userDetails = loadUserByUsername("mail@mail.com");
        assertUserHasAuthorities(userDetails, "ROLE_USER");
    }

    @Test
    void whenLoadedAdminUserByUsername_AdminRoleHasGranted() {
        createAdminUserWithUsername("Admin");
        CustomUserDetails userDetails = loadUserByUsername("Admin");
        assertUserHasAuthorities(userDetails,"ROLE_ADMIN");
    }

    @Test
    void whenLoadedAdminUserByEmail_AdminRoleHasGranted() {
        createAdminUserWithEmail("admin@mail.mail");
        CustomUserDetails userDetails = loadUserByUsername("admin@mail.mail");
        assertUserHasAuthorities(userDetails,"ROLE_ADMIN");
    }

    private void assertUserHasAuthorities(CustomUserDetails userDetails, String... authorities) {
        Set<String> requiredAuthorities = new HashSet<>(Arrays.asList(authorities));
        Set<String> userAuthorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (!userAuthorities.containsAll(requiredAuthorities)) {
            throw new AssertionError("User does not have all the required authorities. Expected: " +
                    Arrays.toString(authorities) + ", but has: " + userAuthorities);
        }
    }

    private void assertUserDoesNotHaveAuthorities(CustomUserDetails userDetails, String... authorities) {
        Set<String> forbiddenAuthorities = new HashSet<>(Arrays.asList(authorities));
        Set<String> userAuthorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        for (String authority : forbiddenAuthorities) {
            if (userAuthorities.contains(authority)) {
                throw new AssertionError("User have forbidden authority: " + authority);
            }
        }
    }

    private void createRegularUserWithUsername(String username) {
        userFixture.createRegularUserWithUsername(username);
    }

    private void createAdminUserWithUsername(String username) {
        userFixture.createAdminUserWithUsername(username);
    }

    private void createRegularUserWithEmail(String email) {
        userFixture.createRegularUserWithEmail(email);
    }

    private void createAdminUserWithEmail(String email) {
        userFixture.createAdminUserWithEmail(email);
    }

    private CustomUserDetails loadUserByUsername(String usernameOrEmail) {
        return detailsService.loadUserByUsername(usernameOrEmail);
    }
}
