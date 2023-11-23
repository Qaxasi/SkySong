package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CookieDeleterImplTest {
    @InjectMocks
    private CookieDeleterImpl cookieDeleter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;


}
