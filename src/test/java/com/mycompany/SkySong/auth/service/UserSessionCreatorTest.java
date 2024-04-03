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

import static org.assertj.core.api.Assertions.assertThat;

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
        String sessionToken = "2eds2etfghthheyyyjh536t3fasd235teg";
        userSessionCreator.createUserSession(loginHelper.loginInvalidPassword("User"), sessionToken);
        assertThat(sessionChecker.userHasSession("User")).isFalse();
    }

    @Test
    void whenSessionCreated_SessionIsStored() {
        String sessionToken = "2eds2etfghthheyyyjh536t3fasd235teg";
        userSessionCreator.createUserSession(loginHelper.login("User"), sessionToken);
        assertThat(sessionChecker.userHasSession("User")).isTrue();
    }

    @Test
    void whenSessionCreated_UserIdIsCorrect() {
        String sessionToken = "2eds2etfghthheyyyjh536t3fasd235teg";
        userSessionCreator.createUserSession(loginHelper.login("User"), sessionToken);
        Session session = sessionFetcher.fetchSessionForToken("2eds2etfghthheyyyjh536t3fasd235teg");
        assertThat(session.getUserId()).isEqualTo(1);
    }

    private void createUserSession(LoginRequest request) {
        userSessionCreator.createUserSession(request, "2eds2etfghthheyyyjh536t3fasd235teg");
    }

    private int sessionUserId() {
        Session session = sessionFetcher.fetchSessionForToken("2eds2etfghthheyyyjh536t3fasd235teg");
        return session.getUserId();
    }
}
