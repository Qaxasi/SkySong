package com.mycompany.SkySong.application.registration.usecase;

import com.mycompany.SkySong.application.registration.dto.UserRegistrationDto;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.domain.registration.exception.CredentialValidationException;
import com.mycompany.SkySong.domain.shared.enums.UserRole;
import com.mycompany.SkySong.infrastructure.persistence.dao.RoleDAO;
import com.mycompany.SkySong.infrastructure.persistence.dao.UserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserExistenceChecker;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.auth.common.UserRegistrationData;
import com.mycompany.SkySong.testsupport.auth.service.UserRoleChecker;
import com.mycompany.SkySong.testsupport.common.BaseIT;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationTest extends BaseIT {

    @Autowired
    private UserRegistrationHandler registration;
    @Autowired
    private UserRoleChecker roleChecker;
    @Autowired
    private UserExistenceChecker userChecker;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserDAO userDAO;
    private UserFixture userFixture;
    private UserRegistrationData registrationData;

    @BeforeEach
    void setup() {
        registrationData = new UserRegistrationData();
        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);
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

    @Test
    void whenDataValidationFails_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> registerUser(registrationData.usernameToShort));
    }

    @Test
    void whenUsernameExist_ThrowException() {
        userFixture.createUserWithUsername("Maks");
        assertThrows(CredentialValidationException.class, () -> registerUser(registrationData.withUsername("Maks")));
    }

    @Test
    void whenEmailExist_ThrowException() {
        userFixture.createUserWithEmail("maks@mail.mail");
        assertThrows(CredentialValidationException.class, () -> registerUser(registrationData.withEmail("maks@mail.mail")));
    }

    private ApiResponse registerUser(UserRegistrationDto registrationDto) {
       return registration.registerUser(registrationDto);
    }
}
