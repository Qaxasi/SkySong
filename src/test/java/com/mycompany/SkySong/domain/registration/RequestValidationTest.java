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
        assertThrows(CredentialValidationException.class, () -> validate(requests.requestWithUsername("Alex")));

    }
    @Test
    void whenUserWithEmailExists_ThrowException() {
        createExistingUserWithEmail("alex@mail.com");
        assertThrows(CredentialValidationException.class, () -> validate(requests.requestWithEmail("alex@mail.com")));
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
