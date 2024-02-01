package com.mycompany.SkySong.auth.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
public class PasswordServiceTest {
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    void whenEncodePassword_MatchesOriginal() {
        String password = "testPassword";
        String encodedPassword = passwordService.encodePassword(password);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }
}
