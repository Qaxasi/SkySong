package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.shared.service.ApplicationMessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;

public class EmailValidationStrategyTest {

    private ApplicationMessageService message;

    private EmailValidationStrategy strategy;

    @BeforeEach
    void setUp() {
        message = new ApplicationMessageServiceImpl();
        strategy = new EmailValidationStrategy(message);
    }
}
