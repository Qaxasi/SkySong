package com.mycompany.SkySong.shared.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ApplicationMessageServiceImplTest {

    @Test
    void shouldThrowExceptionWhenPropertiesFileNotFound() {
        InputStream invalidPropertiesStream = this.getClass().getClassLoader()
                .getResourceAsStream("nonexistent.properties");

        assertThrows(RuntimeException.class,
                () -> new ApplicationMessageServiceImpl(invalidPropertiesStream));
    }
    @Test
    void shouldReturnErrorMessageWhenPropertiesFileNotFound() {
        InputStream invalidPropertiesStream = this.getClass().getClassLoader()
                .getResourceAsStream("nonexistent.properties");

        Exception exception = assertThrows(RuntimeException.class,
                () -> new ApplicationMessageServiceImpl(invalidPropertiesStream));

        assertTrue(exception.getMessage().contains("Failed to load"));
    }
}
