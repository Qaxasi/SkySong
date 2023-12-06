package com.mycompany.SkySong.shared.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
}
