package com.mycompany.SkySong.application.registration.usecase;

import com.mycompany.SkySong.application.shared.dto.ApiResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void whenRegistrationSuccess_ReturnMessage () {
      ApiResponse response = registration.registerUser(requests.validRequest());
      assertEquals("Your registration was successful!" , response.message());
    }

    @Test
    void whenUserRegistered_UserExist() {
        registration.registerUser(requests.requestWithUsername("Maks"));
        assertTrue(userChecker.userExist("Maks"));
    }
}
