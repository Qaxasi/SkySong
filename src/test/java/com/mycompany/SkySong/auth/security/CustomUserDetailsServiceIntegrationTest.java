package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.repository.UserDAO;
import com.mycompany.SkySong.testsupport.auth.security.UserDetailsAssertions;
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
    void cleanup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
    }
    @Test
    void shouldReturnCorrectUserDetailsWhenLoggingWithUsername() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUsername");

        UserDetailsAssertions.assertUserDetails(
                userDetails, passwordEncoder, "testUsername", "testPassword@123");
    }

    @Test
    void shouldReturnCorrectUserDetailsWhenLoggingWithEmail() {
        //In our implementation, the email can serve as the username
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testEmail@gmail.com");

        UserDetailsAssertions.assertUserDetails(
                userDetails, passwordEncoder, "testEmail@gmail.com", "testPassword@123");
    }
    @Test
    void shouldHaveUserRoleWhenLoadedRegularUserByUsername() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUsername");

        UserDetailsAssertions.hasAuthority(userDetails, "ROLE_USER");
    }
    @Test
    void shouldHaveUserRoleWhenLoadedRegularUserByEmail() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testEmail@gmail.com");

        UserDetailsAssertions.hasAuthority(userDetails, "ROLE_USER");
    }
    @Test
    void shouldHaveUserAndAdminRolesWhenLoadedAdminByUsername() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testAdmin");

        UserDetailsAssertions.hasAuthority(userDetails, "ROLE_USER", "ROLE_ADMIN");
    }
    @Test
    void shouldHaveUserAndAdminRolesWhenLoadedAdminByEmail() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testAdmin@gmail.com");

        UserDetailsAssertions.hasAuthority(userDetails, "ROLE_USER", "ROLE_ADMIN");
    }
    @Test
    void shouldNotHaveAdminRoleForRegularUser() {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUsername");

        assertFalse(UserDetailsAssertions.hasAuthority(userDetails, "ROLE_ADMIN"));
    }
}
