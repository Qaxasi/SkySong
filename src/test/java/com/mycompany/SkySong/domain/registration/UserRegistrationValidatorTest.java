package com.mycompany.SkySong.domain.registration;

import com.mycompany.SkySong.application.registration.dto.UserRegistrationDto;
import com.mycompany.SkySong.domain.registration.exception.CredentialValidationException;
import com.mycompany.SkySong.domain.registration.service.UserRegistrationValidator;
import com.mycompany.SkySong.infrastructure.dao.InMemoryRoleDAO;
import com.mycompany.SkySong.infrastructure.dao.InMemoryUserDAO;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserRegistrationData;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.mycompany.SkySong.testsupport.assertions.CustomAssertions.assertErrorMessage;
import static com.mycompany.SkySong.testsupport.assertions.ExceptionAssertionUtils.assertException;
import static org.junit.Assert.assertThrows;

class UserRegistrationValidatorTest {

    private UserRegistrationData registrationData;
    private InMemoryUserDAO userDAO;
    private InMemoryRoleDAO roleDAO;
    private UserFixture userFixture;
    private UserRegistrationValidator validation;

    @BeforeEach
    void setUp() {
        roleDAO = new InMemoryRoleDAO();
        userDAO = new InMemoryUserDAO(roleDAO);

        validation = new UserRegistrationValidator(userDAO);

        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);

        registrationData = new UserRegistrationData();
    }

    @AfterEach
    void cleanUp() {
        roleDAO.clear();
        userDAO.clear();
    }

    @Test
    void whenUserWithUsernameExists_ThrowException() {
        createExistingUserWithUsername("Alex");
        assertException(() -> validate(registrationData.withUsername("Alex")),
                CredentialValidationException.class, "Username is already exist!.");
    }
    @Test
    void whenUserWithEmailExists_ThrowException() {
        createExistingUserWithEmail("alex@mail.com");
        assertException(() -> validate(registrationData.withEmail("alex@mail.com")),
                CredentialValidationException.class, "Email is already exist!.");
    }

    @Test
    void whenInvalidPasswordFormat_ReturnErrorMessage() {
        assertErrorMessage(() -> validate(registrationData.passwordToShort), "Invalid password format. The password" +
                " must contain an least 8 characters, including uppercase letters, lowercase letters, numbers, and special characters.");
    }

    @Test
    void whenInvalidUsernameFormat_ReturnErrorMessage() {
        assertErrorMessage(() -> validate(registrationData.usernameToShort), "Invalid username format. The username can " +
                "contain only letters and numbers, and should be between 3 and 20 characters long.");
    }

    @Test
    void whenInvalidEmailFormat_ReturnErrorMessage() {
        assertErrorMessage(() -> validate(registrationData.emailToShort), "Invalid email address format. The email should " +
                "follow the standard format (e.g., user@example.com) and be between 6 and 30 characters long.");
    }
    
    @Test
    void whenPasswordToShort_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(registrationData.passwordToShort));
    }

    @Test
    void whenPasswordNoHaveUppercaseLetter_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(registrationData.passwordNoUppercaseLetter));
    }

    @Test
    void whenPasswordNoHaveLowercaseLetter_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(registrationData.passwordNoLowercaseLetter));
    }

    @Test
    void whenPasswordNoHaveSpecialCharacter_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(registrationData.passwordNoSpecialCharacter));
    }

    @Test
    void whenPasswordNoHaveNumber_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(registrationData.passwordNoNumber));
    }

    @Test
    void whenUsernameIsToShort_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(registrationData.usernameToShort));
    }

    @Test
    void whenUsernameIsToLong_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(registrationData.usernameToLong));
    }

    @Test
    void whenUsernameHaveSpecialCharacter_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(registrationData.usernameWithSpecialCharacter));
    }

    @Test
    void whenEmailHaveInvalidFormat_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(registrationData.emailInvalidFormat));
    }

    @Test
    void whenEmailIsToShort_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(registrationData.emailToShort));
    }

    @Test
    void whenEmailIsToLong_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(registrationData.emailToLong));
    }

    private void validate(UserRegistrationDto registrationDto) {
        validation.validate(registrationDto);
    }

    private void createExistingUserWithUsername(String username) {
        userFixture.createUserWithUsername(username);
    }

    private void createExistingUserWithEmail(String email) {
        userFixture.createUserWithEmail(email);
    }
}
