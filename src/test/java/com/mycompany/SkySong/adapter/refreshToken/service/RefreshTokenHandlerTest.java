package com.mycompany.SkySong.adapter.refreshToken.service;

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

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

        InMemoryRoleDAO roleDAO = new InMemoryRoleDAO();
        InMemoryUserDAO userDAO = new InMemoryUserDAO(roleDAO);
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService(userDAO, roleDAO);

        tokenHandler = new RefreshTokenHandler(tokenManager, userDetailsService);

        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);

        jwtTokenGenerator = new TestJwtTokenGenerator();
    }

    @AfterEach
    void clean() {
        roleDAO.clear();
        userDAO.clear();
    }

    
}
