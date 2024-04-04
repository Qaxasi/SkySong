package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.dto.LoginRequest;
import com.mycompany.SkySong.auth.model.entity.Session;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import com.mycompany.SkySong.testsupport.auth.common.TestUserFactory;
import com.mycompany.SkySong.testsupport.auth.security.SessionFetcher;
import com.mycompany.SkySong.testsupport.auth.security.UserSessionChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserSessionCreatorTest extends BaseIT {

    @Autowired
    private UserSessionCreator userSessionCreator;
    @Autowired
    private LoginRequests loginHelper;
    @Autowired
    private UserSessionChecker sessionChecker;
    @Autowired
    private SessionFetcher sessionFetcher;
    @Autowired
    private TestUserFactory userFactory;

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
    void whenAuthenticationFails_SessionNotCreated() {
        createSession(loginHelper.loginInvalidPassword("Tom"));
        assertThat(sessionChecker.userHasSession("Tom")).isFalse();
    }

    @Test
    void whenSessionCreated_SessionIsStored() {
        userFactory.buildUser(1, "User");
        createUserSession(loginHelper.login("User"));
        assertThat(sessionChecker.userHasSession("User")).isTrue();
    }

    @Test
    void whenSessionCreated_UserIdIsCorrect() {
        userFactory.buildUser(1, "User");
        createUserSession(loginHelper.login("User"));
        assertThat(sessionUserId()).isEqualTo(1);
    }

    private void createUserSession(LoginRequest request) {
        userSessionCreator.createUserSession(request, "2eds2etfghthheyyyjh536t3fasd235teg");
    }

    private int sessionUserId() {
        Session session = sessionFetcher.fetchSessionForToken("2eds2etfghthheyyyjh536t3fasd235teg");
        return session.getUserId();
    }
}
