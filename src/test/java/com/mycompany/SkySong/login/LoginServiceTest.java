package com.mycompany.SkySong.login;

import com.mycompany.SkySong.login.domain.service.UserSessionService;
import com.mycompany.SkySong.registration.domain.model.User;
import com.mycompany.SkySong.testsupport.auth.common.LoginRequests;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserSaver;
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
    private LoginRequests loginRequests;
    @Autowired
    private UserBuilder builder;
    @Autowired
    private UserSaver userSaver;

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
        User user = builder.buildByUsername("User").build();
        userSaver.save(user);

        String token = session.createSession(loginRequests.login("User"));

        assertThat(token).isNotNull();
    }
}
