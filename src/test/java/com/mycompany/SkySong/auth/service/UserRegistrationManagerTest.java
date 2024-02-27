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

public class UserRegistrationManagerTest extends BaseIT {

    @Autowired
    private UserRegistrationManager registrationManager;
    @Autowired
    private UserExistenceChecker userChecker;
    @Autowired
    private UserRoleChecker roleChecker;

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
    void whenUserRegistered_UserExist() {
        registrationManager.setupNewUser(RegistrationRequests.REGISTER("Maks"));
        assertThat(userChecker.userExist("Maks")).isTrue();
    }

    @Test
    void whenUserRegistered_UserHasRole() {
        registrationManager.setupNewUser(RegistrationRequests.REGISTER("Maks"));
        assertTrue(roleChecker.hasUserRole("Maks", UserRole.ROLE_USER.name()));
    }
}
