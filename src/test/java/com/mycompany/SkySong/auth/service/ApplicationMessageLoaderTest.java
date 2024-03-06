package com.mycompany.SkySong.auth.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Test
    void whenApplicationStarts_MessageServiceIsInjected() {
        assertNotNull(messageService);
    }

    @Test
    void whenKeyExist_ReturnMessage() {
        String key = "user.id.required";
        String expectedMessage = "User ID is required and cannot be empty.";

        assertEquals(expectedMessage, messageService.getMessage(key));
    }

    @Test
    void whenKeyNotExist_ReturnDefaultMessage() {
        String key = "key.not.exist";
        String expectedMessage = "Default message for " + key;

        assertEquals(expectedMessage, messageService.getMessage(key));
    }

    @Test
    void whenFormattingMessageWithArguments_ReturnsFormattedMessage() {
        long id = 100L;
        String key = "user.delete.not-found";
        String expectedMessage = "User with ID 100 does not exist.";

        assertEquals(expectedMessage, messageService.getMessage(key, id));
    }
}
