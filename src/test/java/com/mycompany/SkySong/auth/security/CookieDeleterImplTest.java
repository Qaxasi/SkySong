package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CookieDeleterImplTest {
    @InjectMocks
    private CookieDeleterImpl cookieDeleter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Test
    void shouldDeleteNamedCookieWhenPresent() {
        Cookie[] cookies = { new Cookie("testCookie", "testValue")};
        when(request.getCookies()).thenReturn(cookies);

        cookieDeleter.deleteCookie(request, response, "testCookie");

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        Cookie modifiedCookie = cookieCaptor.getValue();

        assertAll("modifiedCookie",
                () -> assertEquals("testCookie", modifiedCookie.getName()),
                () -> assertNull(modifiedCookie.getValue()),
                () -> assertEquals(0, modifiedCookie.getMaxAge()));
    }
}
