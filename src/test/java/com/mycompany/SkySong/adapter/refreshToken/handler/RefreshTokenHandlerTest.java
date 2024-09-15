package com.mycompany.SkySong.adapter.refreshToken.service;

import com.mycompany.SkySong.adapter.refreshToken.handler.RefreshTokenHandler;
import com.mycompany.SkySong.adapter.security.jwt.JwtTokenManager;
import com.mycompany.SkySong.adapter.security.user.CustomUserDetailsService;
import com.mycompany.SkySong.infrastructure.dao.InMemoryRoleDAO;
import com.mycompany.SkySong.infrastructure.dao.InMemoryUserDAO;
import com.mycompany.SkySong.testsupport.auth.common.TestJwtTokenGenerator;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RefreshTokenHandlerTest {

    private RefreshTokenHandler tokenHandler;
    private InMemoryRoleDAO roleDAO;
    private InMemoryUserDAO userDAO;
    private UserFixture userFixture;
    private TestJwtTokenGenerator jwtTokenGenerator;

    @BeforeEach
    void setUp() {
        String secretKey = "wJ4ds7VbRmFHRP4fX5QbJmTcYZv5P1ZkVN7/kO4id8E=";
        long jwtExpiration = 600000;
        long refreshJwtExpiration = 86400000;
        JwtTokenManager tokenManager = new JwtTokenManager(secretKey, jwtExpiration, refreshJwtExpiration);

        roleDAO = new InMemoryRoleDAO();
        userDAO = new InMemoryUserDAO(roleDAO);
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService(userDAO, roleDAO);

        tokenHandler = new RefreshTokenHandler(tokenManager, userDetailsService);

        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);

        jwtTokenGenerator = new TestJwtTokenGenerator(secretKey, jwtExpiration, refreshJwtExpiration);
    }

    @AfterEach
    void clean() {
        roleDAO.clear();
        userDAO.clear();
    }

    @Test
    void whenInvalidToken_ValidationFailure() {
        String refreshToken = "invalid";
        assertThrows(IllegalArgumentException.class, () -> tokenHandler.generateAccessTokenFromRefreshToken(refreshToken));
    }

    @Test
    void whenValidRefreshToken_GenerateAccessToken() {
        String refreshToken = generateValidRefreshToken();
        String accessToken = tokenHandler.generateAccessTokenFromRefreshToken(refreshToken);
        assertThat(accessToken).isNotEmpty();
    }

    private String generateValidRefreshToken() {
        userFixture.createUserWithUsername("Alex");
        return jwtTokenGenerator.generateValidRefreshToken("Alex");
    }
}
