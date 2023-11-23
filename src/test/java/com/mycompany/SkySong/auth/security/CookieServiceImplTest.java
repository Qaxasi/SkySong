package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CookieServiceImplTest {
    @InjectMocks
    private CookieServiceImpl cookieService;
    @Mock
    private HttpServletRequest request;
}
