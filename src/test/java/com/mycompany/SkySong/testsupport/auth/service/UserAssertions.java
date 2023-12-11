package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class UserAssertions {
    public static void assertValidationException(Executable executable) {
        assertThrows(CredentialValidationException.class, executable);
    }
    public static void assertErrorMessage(Executable executable, String expectedMessage) {
        Exception exception = assertThrows(Exception.class, executable);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
