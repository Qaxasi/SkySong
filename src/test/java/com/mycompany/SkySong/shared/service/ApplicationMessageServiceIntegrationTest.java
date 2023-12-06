package com.mycompany.SkySong.shared.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApplicationMessageServiceIntegrationTest {
    @Autowired
    private ApplicationMessageService messageService;
    @Test
    void shouldBeInjectedCorrectly() {
        assertNotNull(messageService);
    }
    @Test
    void shouldReturnCorrectMessageForExistingKey() {
        String key = "user.id.required";
        String expectedMessage = "User ID is required and cannot be empty.";

        assertEquals(expectedMessage, messageService.getMessage(key));
    }
    @Test
    void shouldReturnDefaultMessageForNonExistingKey() {
        String key = "key.not.exist";
        String expectedMessage = "Default message for " + key;
        assertEquals(expectedMessage, messageService.getMessage(key));
    }
    @Test
    void shouldFormatMessageCorrectly() {
        long id = 100L;
        String key = "user.delete.not-found";
        String expectedMessage = "User with ID 100 does not exist.";

        assertEquals(expectedMessage, messageService.getMessage(key, id));
    }
}
