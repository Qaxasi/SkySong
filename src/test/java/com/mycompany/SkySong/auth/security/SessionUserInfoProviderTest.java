package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import com.mycompany.SkySong.auth.exception.SessionNotFoundException;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class SessionUserInfoProviderTest extends BaseIT {

    @Autowired
    private SessionUserInfoProvider userInfoProvider;

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
    void whenSessionIdExist_GetAndReturnUsername() {
        String sessionId = "jrYa_WLToysV-r08qLhwUZncJLY8OPgT";
        String username = userInfoProvider.getUsernameForSession(sessionId);
        assertThat(username).isEqualTo("User");
    }

    @Test
    void whenSessionIdNotExist_ThrowException() {
        String sessionId = "jrYa_WLToysV-r08invalidZncJLY8OPgT";
        assertThrows(SessionNotFoundException.class,
                () -> userInfoProvider.getUsernameForSession(sessionId));
    }
}

