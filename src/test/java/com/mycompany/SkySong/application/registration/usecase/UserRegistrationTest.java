package com.mycompany.SkySong.application.registration.usecase;

import com.mycompany.SkySong.application.registration.dto.UserRegistrationDto;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.common.UserRegistrationData;
import com.mycompany.SkySong.testsupport.auth.service.UserRoleChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRegistrationTest extends BaseIT {

    @Autowired
    private UserRegistrationHandler registration;
    @Autowired
    private UserRoleChecker roleChecker;
    @Autowired
    private UserExistenceChecker userChecker;
    private UserRegistrationData registrationData;

    @BeforeEach
    void setup() {
        registrationData = new UserRegistrationData();
    }

    @Test
    void whenRegistrationSuccess_ReturnMessage () {
      ApiResponse response = registerUser(registrationData.validData());
      assertEquals("Your registration was successful!" , response.message());
    }

    @Test
    void whenValidData_UserRegistered() {
        registerUser(registrationData.withUsername("Maks"));
        assertTrue(userChecker.userExist("Maks"));
    }

    @Test
    void whenUserRegistered_UserHasRole() {
        registerUser(registrationData.withUsername("Maks"));
        assertTrue(roleChecker.hasUserRole("Maks", UserRole.ROLE_USER.name()));
    }

    private ApiResponse registerUser(UserRegistrationDto registrationDto) {
       return registration.registerUser(registrationDto);
    }
}
