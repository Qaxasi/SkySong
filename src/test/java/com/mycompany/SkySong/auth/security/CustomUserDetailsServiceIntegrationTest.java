package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.repository.UserDAO;
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
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/test-data-setup.sql"));
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
    void shouldHaveUserRoleWhenLoadedRegularUserByUsername() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUsername");

        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }
    @Test
    void shouldHaveUserRoleWhenLoadedRegularUserByEmail() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testEmail@gmail.com");

        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }
    @Test
    void shouldHaveUserAndAdminRolesWhenLoadedAdminByUsername() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testAdmin");

        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> "ROLE_USER".equals(auth.getAuthority())));
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority())));
    }
    @Test
    void shouldHaveUserAndAdminRolesWhenLoadedAdminByEmail() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testAdmin@gmail.com");

        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> "ROLE_USER".equals(auth.getAuthority())));
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority())));
    }
    @Test
    void shouldNotHaveAdminRoleForRegularUser() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUsername");

        assertFalse(userDetails.getAuthorities().stream().anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority())));
    }
}
