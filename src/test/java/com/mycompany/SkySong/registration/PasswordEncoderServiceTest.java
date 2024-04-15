package com.mycompany.SkySong.registration;

import com.mycompany.SkySong.registration.PasswordEncoderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PasswordEncoderServiceTest {

    @Autowired
    private PasswordEncoderService passwordEncoderService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void whenEncodePassword_MatchesOriginal() {
        String password = "testPassword";
        String encodedPassword = passwordEncoderService.encodePassword(password);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }
}
