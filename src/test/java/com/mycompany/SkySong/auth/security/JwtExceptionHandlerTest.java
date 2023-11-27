package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JwtExceptionHandlerTest {
    @InjectMocks
    private JwtExceptionHandler jwtExceptionHandler;
    @Mock
    private ApplicationMessageService messageService;

}
