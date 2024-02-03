package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import com.mycompany.SkySong.shared.service.ApplicationMessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;

public class PasswordValidationStrategyTest {

    private ApplicationMessageService message;

    private PasswordValidationStrategy strategy;

    @BeforeEach
    void setUp() {
        message = new ApplicationMessageServiceImpl();
        strategy = new PasswordValidationStrategy(message);
    }

}
