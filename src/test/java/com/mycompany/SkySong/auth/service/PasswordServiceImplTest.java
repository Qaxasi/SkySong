package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class PasswordServiceImplTest {
    @InjectMocks
    private PasswordServiceImpl passwordService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ApplicationMessageService messageService;
}
