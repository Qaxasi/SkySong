package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CookieAdderImplTest {
    @InjectMocks
    private CookieAdderImpl cookieAdder;
    @Mock
    private HttpServletResponse response;

    @Test
    void shouldCreateCookieWithCorrectProperties() {
        cookieAdder.addCookie(response,  "testCookie", "testValue", 3600);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie capturedCookie = cookieCaptor.getValue();
        assertAll("cookie",
                () -> assertEquals("testCookie", capturedCookie.getName()),
                () -> assertEquals("testValue", capturedCookie.getValue()),
                () -> assertEquals(3600, capturedCookie.getMaxAge()),
                () -> assertEquals("/", capturedCookie.getPath()),
                () -> assertTrue(capturedCookie.isHttpOnly()),
                () -> assertTrue(capturedCookie.getSecure()));
    }
    @Test
    void shouldHandleNullResponse() {
        assertThrows(NullPointerException.class, () -> cookieAdder.addCookie(
                null, "testCookie", "testValue",  3600));
    }
}
