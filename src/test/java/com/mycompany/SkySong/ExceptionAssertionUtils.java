package com.mycompany.SkySong;

import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionAssertionUtils {
    public static void assertException(Executable executable, Class<? extends Throwable> expectedException,
                                       String expectedMessage) {
        Throwable exception = assertThrows(expectedException, executable);
        assertEquals(expectedMessage, exception.getMessage());

    }
}
