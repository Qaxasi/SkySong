package com.mycompany.SkySong.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class JwtAuthenticationFilterIntegrationTest {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtTokenProviderImpl jwtTokenProviderImpl;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @MockBean
    private FilterChain filterChain;
    @Autowired
    private DataSource dataSource;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    @BeforeEach
    void setUp() throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/test-data-setup.sql"));
        }
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }
    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
    }
    @Test
    void shouldContinueWithFilterChainAfterSuccessfulAuthentication() throws ServletException, IOException {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUsername", null);

        String token = jwtTokenProviderImpl.generateToken(authentication);
        Cookie cookie = new Cookie("auth_token", token);
        request.setCookies(cookie);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
    @Test
    void shouldSetSecurityContextAfterSuccessfulAuthentication() throws ServletException, IOException {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUsername", null);

        String token = jwtTokenProviderImpl.generateToken(authentication);
        Cookie cookie = new Cookie("auth_token", token);
        request.setCookies(cookie);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authContext);
    }
    @Test
    void shouldSetCorrectUsernameInSecurityContextAfterSuccessfulAuthentication() throws ServletException, IOException {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUsername", null);

        String token = jwtTokenProviderImpl.generateToken(authentication);
        Cookie cookie = new Cookie("auth_token", token);
        request.setCookies(cookie);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        assertEquals(authentication.getName(), authContext.getName());
    }
    @Test
    void shouldNotSetSecurityContextForRequestWhenUserNotFound() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "testAbsentUsername", null);

        String token = jwtTokenProviderImpl.generateToken(authentication);
        Cookie cookie = new Cookie("auth_token", token);
        request.setCookies(cookie);

        assertThrows(UsernameNotFoundException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        assertNull(authContext);
    }
    @Test
    void shouldNotProcessRequestWhenUserNotFound() throws ServletException, IOException {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "testAbsentUsername", null);

        String token = jwtTokenProviderImpl.generateToken(authentication);
        Cookie cookie = new Cookie("auth_token", token);
        request.setCookies(cookie);

        assertThrows(UsernameNotFoundException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, filterChain));

        verify(filterChain, never()).doFilter(request, response);
    }
}
