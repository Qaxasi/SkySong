package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.exception.UserNotFoundException;
import com.mycompany.SkySong.authentication.model.entity.Role;
import com.mycompany.SkySong.authentication.model.entity.User;
import com.mycompany.SkySong.authentication.model.entity.UserRole;
import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.secutiry.service.CustomUserDetailsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CustomUserDetailsServiceIntegrationTest {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @BeforeEach
    void init() throws Exception {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/user-data.sql"));
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/role-data.sql"));

        }
    }
    @AfterEach
    void cleanup() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
    }
    @Test
    void shouldReturnCorrectUserDetailsWhenLoggingWithUsername() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUsername");

        assertEquals("testUsername", userDetails.getUsername());
        assertTrue(passwordEncoder.matches("testPassword@123", userDetails.getPassword()));
    }

    @Test
    void shouldReturnCorrectUserDetailsWhenLoggingWithEmail() {
        //In our implementation, the email can serve as the username
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testEmail@gmail.com");

        assertEquals("testEmail@gmail.com", userDetails.getUsername());
        assertTrue(passwordEncoder.matches("testPassword@123", userDetails.getPassword()));
    }
    @Test
    void shouldThrowExceptionWhenLoadingMissingUserByUsername() {
       String nonExistentUsername = "nonExistentUsername";

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(nonExistentUsername));
    }
    @Test
    void shouldThrowExceptionWhenLoadingMissingUserByEmail() {
        String nonExistentEmail = "nonExistentEmail@gmail.com";

        //In our implementation, the email can serve as the username
        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername(nonExistentEmail));
    }
    @Test
    void shouldFetchUserDetailsWithCorrectRolesBasedOnUsername() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUsername");

        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }
    @Test
    void shouldFetchUserDetailsWithCorrectRolesBasedOnEmail() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testEmail@gmail.com");

        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }
    @Test
    void shouldHaveUserAndAdminRolesWhenLoadedByUsername() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testAdmin");

        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> "ROLE_USER".equals(auth.getAuthority())));
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority())));
    }
    @Test
    void shouldHaveUserAndAdminRolesWhenLoadedByEmail() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testAdmin@gmail.com");

        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> "ROLE_USER".equals(auth.getAuthority())));
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority())));
    }
}
