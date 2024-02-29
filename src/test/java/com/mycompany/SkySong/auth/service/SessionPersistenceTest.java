package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.security.SessionPersistence;
import com.mycompany.SkySong.testsupport.auth.security.SessionExistenceChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class SessionPersistenceTest extends BaseIT {

    @Autowired
    private SessionPersistence sessionPersistence;
    @Autowired
    private SessionExistenceChecker sessionChecker;

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
}
