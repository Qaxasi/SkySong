package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.model.entity.UserRole;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.service.UserRoleChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseCleaner;
import com.mycompany.SkySong.testsupport.common.SqlDatabaseInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRegistrationTest extends BaseIT {

    @Autowired
    private UserRegistration registration;
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

    @Test
    void whenUserRegistered_UserExist() {
        registration.registerUser(requests.register("Maks"));
        assertThat(userChecker.userExists("Maks")).isTrue();
    }

    @Test
    void whenUserRegistered_UserHasRole() {
        registration.registerUser(requests.register("Maks"));
        assertTrue(roleChecker.hasUserRole("Maks", UserRole.ROLE_USER.name()));
    }
}
