package com.mycompany.SkySong.application.registration.usecase;

import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.service.UserRoleChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

class UserRegistrationHandlerTest extends BaseIT {

    @Autowired
    private UserRegistrationHandler registration;
    @Autowired
    private UserExistenceChecker userChecker;
    @Autowired
    private UserRoleChecker roleChecker;
    private RegistrationRequests requests;

    @Autowired
    private SqlDatabaseInitializer initializer;
    @Autowired
    private SqlDatabaseCleaner cleaner;

    @BeforeEach
    void setUp() throws Exception {
        requests = new RegistrationRequests();
        initializer.setup("data_sql/test-setup.sql");
    }

    @AfterEach
    void cleanUp() {
        cleaner.clean();
    }
}
