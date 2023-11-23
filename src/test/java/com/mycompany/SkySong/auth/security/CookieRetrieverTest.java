package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CookieRetrieverTest {
    @InjectMocks
    private CookieRetriever cookieRetriever;
    @Mock
    private HttpServletRequest request;

    @Test
    void shouldReturnEmptyOptionalWhenNoCookieRequest() {
        when(request.getCookies()).thenReturn(null);

        Optional<Cookie> result = cookieRetriever.getCookie(request, "testCookie");

        assertEquals(result, Optional.empty());
    }

    @Test
    void shouldReturnEmptyOptionalWhenRequestedCookieNotInRequest() {
        Cookie[] cookies = {new Cookie("differentName", "testValue")};
        when(request.getCookies()).thenReturn(cookies);

        Optional<Cookie> result = cookieRetriever.getCookie(request, "testCookie");

        assertEquals(result, Optional.empty());
    }

    @Test
    void shouldReturnCookieWhenRequestedCookieInRequest() {
        Cookie expectedCookie = new Cookie("testCookie", "testValue");
        Cookie[] cookies = {expectedCookie, new Cookie("differentName", "differentValue")};

        when(request.getCookies()).thenReturn(cookies);

        Optional<Cookie> result = cookieRetriever.getCookie(request, "testCookie");

        assertTrue(result.isPresent());
        assertThat(result.get(), is(expectedCookie));
    }
    @Test
    void shouldThrowNullPointerExceptionWhenRequestIsNull() {
        assertThrows(NullPointerException.class, () -> cookieRetriever.getCookie(null, "testCookie"));
    }
}
