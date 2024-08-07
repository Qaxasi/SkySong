package com.mycompany.SkySong.domain.registration;

import com.mycompany.SkySong.application.registration.dto.RegisterRequest;
import com.mycompany.SkySong.domain.registration.exception.CredentialValidationException;
import com.mycompany.SkySong.domain.registration.service.RequestValidation;
import com.mycompany.SkySong.infrastructure.dao.InMemoryRoleDAO;
import com.mycompany.SkySong.infrastructure.dao.InMemoryUserDAO;
import com.mycompany.SkySong.testsupport.auth.common.RegistrationRequests;
import com.mycompany.SkySong.testsupport.auth.common.UserBuilder;
import com.mycompany.SkySong.testsupport.auth.common.UserFixture;
import com.mycompany.SkySong.testsupport.utils.CustomPasswordEncoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.mycompany.SkySong.testsupport.assertions.CustomAssertions.assertErrorMessage;
import static com.mycompany.SkySong.testsupport.assertions.ExceptionAssertionUtils.assertException;
import static org.junit.Assert.assertThrows;

class RequestValidationTest {

    private RegistrationRequests requests;
    private InMemoryUserDAO userDAO;
    private InMemoryRoleDAO roleDAO;
    private UserFixture userFixture;
    private RequestValidation validation;

    @BeforeEach
    void setUp() {
        roleDAO = new InMemoryRoleDAO();
        userDAO = new InMemoryUserDAO(roleDAO);

        validation = new RequestValidation(userDAO);

        CustomPasswordEncoder encoder = new CustomPasswordEncoder(new BCryptPasswordEncoder());
        UserBuilder userBuilder = new UserBuilder(encoder);

        userFixture = new UserFixture(roleDAO, userDAO, userBuilder);

        requests = new RegistrationRequests();
    }

    @AfterEach
    void cleanUp() {
        roleDAO.clear();
        userDAO.clear();
    }

    @Test
    void whenUserWithUsernameExists_ThrowException() {
        createExistingUserWithUsername("Alex");
        assertException(() -> validate(requests.requestWithUsername("Alex")),
                CredentialValidationException.class, "Username is already exist!.");
    }
    @Test
    void whenUserWithEmailExists_ThrowException() {
        createExistingUserWithEmail("alex@mail.com");
        assertException(() -> validate(requests.requestWithEmail("alex@mail.com")),
                CredentialValidationException.class, "Email is already exist!.");
    }

    @Test
    void whenInvalidPasswordFormat_ReturnErrorMessage() {
        assertErrorMessage(() -> validate(requests.passwordToShort), "Invalid password format. The password" +
                " must contain an least 8 characters, including uppercase letters, lowercase letters, numbers, and special characters.");
    }

    @Test
    void whenInvalidUsernameFormat_ReturnErrorMessage() {
        assertErrorMessage(() -> validate(requests.usernameToShort), "Invalid username format. The username can " +
                "contain only letters and numbers, and should be between 3 and 20 characters long.");
    }

    @Test
    void whenInvalidEmailFormat_ReturnErrorMessage() {
        assertErrorMessage(() -> validate(requests.emailToShort), "Invalid email address format. The email should " +
                "follow the standard format (e.g., user@example.com) and be between 6 and 30 characters long.");
    }
    
    @Test
    void whenPasswordToShort_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(requests.passwordToShort));
    }

    @Test
    void whenPasswordNoHaveUppercaseLetter_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(requests.passwordNoUppercaseLetter));
    }

    @Test
    void whenPasswordNoHaveLowercaseLetter_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(requests.passwordNoLowercaseLetter));
    }

    @Test
    void whenPasswordNoHaveSpecialCharacter_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(requests.passwordNoSpecialCharacter));
    }

    @Test
    void whenPasswordNoHaveNumber_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(requests.passwordNoNumber));
    }

    @Test
    void whenUsernameIsToShort_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(requests.usernameToShort));
    }

    @Test
    void whenUsernameIsToLong_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(requests.usernameToLong));
    }

    @Test
    void whenUsernameHaveSpecialCharacter_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(requests.usernameWithSpecialCharacter));
    }

    @Test
    void whenEmailHaveInvalidFormat_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(requests.emailInvalidFormat));
    }

    @Test
    void whenEmailIsToShort_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(requests.emailToShort));
    }

    @Test
    void whenEmailIsToLong_ThrowException() {
        assertThrows(CredentialValidationException.class, () -> validate(requests.emailToLong));
    }

    private void validate(RegisterRequest requests) {
        validation.validate(requests);
    }

    private void createExistingUserWithUsername(String username) {
        userFixture.createUserWithUsername(username);
    }

    private void createExistingUserWithEmail(String email) {
        userFixture.createUserWithEmail(email);
    }
}
