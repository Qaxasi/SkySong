package com.mycompany.SkySong.testsupport.auth.service;

import com.mycompany.SkySong.shared.exception.CredentialValidationException;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class UserAssertions {
    public static void assertException(Executable executable, Class<? extends Throwable> expectedException,
                                       String expectedMessage) {
        Throwable exception = assertThrows(expectedException, executable);
        assertEquals(expectedMessage, exception.getMessage());
    }
}
