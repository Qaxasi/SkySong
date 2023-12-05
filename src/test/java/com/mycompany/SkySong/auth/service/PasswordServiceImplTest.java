package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.InternalErrorException;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PasswordServiceImplTest {
    @InjectMocks
    private PasswordServiceImpl passwordService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ApplicationMessageService messageService;
    @Test
    void shouldThrowInternalErrorExceptionWhenPasswordIsNull() {
        when(passwordEncoder.encode(null)).thenThrow(new IllegalArgumentException("test error"));
        assertThrows(InternalErrorException.class, () -> passwordService.encodePassword(null));
    }
}
