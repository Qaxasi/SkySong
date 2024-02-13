package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.SqlDatabaseCleaner;
import com.mycompany.SkySong.SqlDatabaseInitializer;
import com.mycompany.SkySong.shared.exception.SessionNotFoundException;
import com.mycompany.SkySong.testsupport.BaseIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SessionAuthenticationTest extends BaseIT {

    @Autowired
    private SessionAuthentication authentication;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        SecurityContextHolder.clearContext();
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenAuthenticateUserWithValidSessionId_SetsAuthenticatedTrue() {
        authentication.authenticateUser("jrYa_WLToysV-r08qLhwUZncJLY8OPgT");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertThat(auth.isAuthenticated()).isTrue();
    }

    @Test
    void whenAuthenticateUserWithValidSessionId_SetsCorrectUsername() {
        authentication.authenticateUser("jrYa_WLToysV-r08qLhwUZncJLY8OPgT");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertThat(auth.getName()).isEqualTo("User");
    }

    @Test
    void whenAuthenticateRegularUserWithValidSessionId_AssignsCorrectAuthorities() {
        authentication.authenticateUser("jrYa_WLToysV-r08qLhwUZncJLY8OPgT");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<String> expectedRole = Set.of("ROLE_USER");
        Set<String> userRoles = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(expectedRole).isEqualTo(userRoles);
    }

    @Test
    void whenAuthenticateAdminUserWithValidSessionId_AssignsCorrectAuthorities() {
        authentication.authenticateUser("5JMDsvOSfM9Mf8qt0s_DB1GeUky8LJLU");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<String> expectedRoles = Set.of("ROLE_USER", "ROLE_ADMIN");
        Set<String> userRoles = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertThat(userRoles.containsAll(expectedRoles)).isTrue();
    }

    @Test
    void whenAuthenticateUserWithInvalidSessionId_ThrowException() {
        assertThrows(SessionNotFoundException.class,
                () -> authentication.authenticateUser("jrYa_WLToysVInvalidLhwUZncJLY8OPgT"));
    }
}
