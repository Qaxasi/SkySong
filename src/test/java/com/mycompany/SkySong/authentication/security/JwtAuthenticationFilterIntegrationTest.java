package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.repository.UserDAO;
import com.mycompany.SkySong.authentication.secutiry.JwtAuthenticationFilter;
import com.mycompany.SkySong.authentication.secutiry.JwtTokenProvider;
import com.mycompany.SkySong.authentication.secutiry.service.CustomUserDetailsService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.crypto.SecretKey;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class JwtAuthenticationFilterIntegrationTest {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
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
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/user-data.sql"));
            ScriptUtils.executeSqlScript(connection, new ClassPathResource("data_sql/role-data.sql"));
        }
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

    }
    @AfterEach
    void cleanup() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("DELETE FROM user_roles");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM roles");
    }
    @Test
    void shouldProcessValidJwtTokenAndContinueFilterChain() throws ServletException, IOException {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUsername", null);

        String token = jwtTokenProvider.generateToken(authentication);

        request.addHeader("Authorization", "Bearer " + token);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(any(), any());
    }
    @Test
    void shouldSetSecurityContextWithValidJwtToken() throws ServletException, IOException {
        Authentication authentication = new UsernamePasswordAuthenticationToken("testUsername", null);

        String token = jwtTokenProvider.generateToken(authentication);

        request.addHeader("Authorization", "Bearer " + token);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authContext = SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authContext);
        assertEquals("testUsername", authContext.getName());
    }
}
