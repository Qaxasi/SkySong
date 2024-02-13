package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.SqlDatabaseCleaner;
import com.mycompany.SkySong.SqlDatabaseInitializer;
import com.mycompany.SkySong.testsupport.BaseIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

public class SessionAuthenticationTest extends BaseIT {

    @Autowired
    private SessionAuthentication authentication;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }

    @Test
    void whenValidSessionId_AuthenticateUser() {
        authentication.authenticateUser("jrYa_WLToysV-r08qLhwUZncJLY8OPgT");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertThat(auth.isAuthenticated()).isTrue();
    }

    @Test
    void whenValidSessionId_AuthContainsExpectedUsername() {
        authentication.authenticateUser("jrYa_WLToysV-r08qLhwUZncJLY8OPgT");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertThat(auth.getName()).isEqualTo("User");
    }

    @Test
    void whenValidSessionId_AuthContainsExpectedAuthorities() {
        authentication.authenticateUser("jrYa_WLToysV-r08qLhwUZncJLY8OPgT");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean hasRole = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_USER"::equals);

        assertThat(hasRole).isTrue();
    }
}
