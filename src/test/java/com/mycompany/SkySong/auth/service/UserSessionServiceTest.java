package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import com.mycompany.SkySong.testsupport.auth.common.TestUserFactory;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSessionServiceTest extends BaseIT {

    @Autowired
    private UserSessionService session;
    @Autowired
    private LoginRequests loginHelper;
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
    void whenSessionCreated_ReturnSessionToken() {
        userFactory.buildUser(1, "User");
        String token = session.createSession(loginHelper.login("User"));
        assertThat(token).isNotNull();
    }
}
