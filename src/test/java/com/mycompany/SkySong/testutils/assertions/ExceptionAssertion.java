package com.mycompany.SkySong.testutils.assertions;

import org.junit.Assert;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionAssertion {
    public static void assertException(Executable executable, Class<? extends Throwable> expectedException,
                                       String expectedMessage) {
        Throwable exception = assertThrows(expectedException, executable);
        assertEquals(expectedMessage, exception.getMessage());

    }
    public static void assertErrorMessage(Executable executable, String errorMessage) {
        Exception exception = assertThrows(Exception.class, executable);
        Assert.assertEquals(exception.getMessage(), errorMessage);
    }
}
