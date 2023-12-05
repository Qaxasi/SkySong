package com.mycompany.SkySong.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Profile("test")
public class PasswordServiceImplIntegrationTest {
    @Autowired
    private PasswordService passwordService;
    private PasswordEncoder passwordEncoder;
}
