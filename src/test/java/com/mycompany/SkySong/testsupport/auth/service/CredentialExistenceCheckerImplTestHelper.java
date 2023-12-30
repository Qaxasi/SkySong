package com.mycompany.SkySong.testsupport.auth.service;


import com.mycompany.SkySong.auth.model.dto.RegisterRequest;
import com.mycompany.SkySong.auth.service.CredentialExistenceChecker;
import com.mycompany.SkySong.shared.repository.UserDAO;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CredentialExistenceCheckerImplTestHelper {
    public static void assertUsernameException(UserDAO userDAO,
                                               CredentialExistenceChecker checker,
                                               String username,
                                               boolean existUsername,
                                               Class<? extends Exception> exception) {

        RegisterRequest request = new RegisterRequest(username, "mail@mail.com", "Password#3");

        when(userDAO.existsByUsername(username)).thenReturn(existUsername);

        assertThrows(exception,
                () -> checker.checkForExistingCredentials(request));
    }
    public static void assertEmailException(UserDAO userDAO,
                                            CredentialExistenceChecker checker,
                                            String email,
                                            boolean existEmail,
                                            Class<? extends Exception> exception) {

        RegisterRequest request = new RegisterRequest("User", email, "Password#3");

        when(userDAO.existsByEmail(email)).thenReturn(existEmail);

        assertThrows(exception,
                () -> checker.checkForExistingCredentials(request));
    }
}
