package com.mycompany.SkySong.testsupport.assertions;

import org.junit.jupiter.api.function.Executable;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomAssertions {

    public static void assertErrorMessage(Executable executable, String errorMessage) {
        Exception exception = assertThrows(Exception.class, executable);
        assertEquals(exception.getMessage(), errorMessage);
    }
}
