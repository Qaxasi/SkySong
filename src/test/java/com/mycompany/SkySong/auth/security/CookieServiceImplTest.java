package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CookieServiceImplTest {
    @InjectMocks
    private CookieServiceImpl cookieService;
    @Mock
    private HttpServletRequest request;

    @Test
    void shouldReturnEmptyOptionalWhenNoCookieRequest() {
        when(request.getCookies()).thenReturn(null);

        Optional<Cookie> result = cookieService.getCookie(request, "testCookie");

        assertEquals(result, Optional.empty());
    }
    @Test
    void shouldReturnEmptyOptionalWhenRequestedCookieNotInRequest() {
        Cookie[] cookies = {new Cookie("differentName", "testValue")};
        when(request.getCookies()).thenReturn(cookies);

        Optional<Cookie> result = cookieService.getCookie(request, "testCookie");

        assertEquals(result, Optional.empty());
    }
}
