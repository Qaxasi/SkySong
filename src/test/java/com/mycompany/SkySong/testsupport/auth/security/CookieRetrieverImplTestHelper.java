package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.CookieRetriever;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CookieRetrieverImplTestHelper {
    public static void assertNoCookiesReturnEmptyOptional(CookieRetriever retriever,
                                                          HttpServletRequest request, String cookieName) {
        when(request.getCookies()).thenReturn(null);
        Optional<Cookie> result = retriever.getCookie(request,  cookieName);
        assertEquals(result, Optional.empty());
    }
    public static void assertCookieNotInRequestReturnsEmptyOptional(CookieRetriever cookieRetriever,
                                                                    HttpServletRequest request,
                                                                    Cookie[] cookies,
                                                                    String requestedCookieName) {
        when(request.getCookies()).thenReturn(cookies);
        Optional<Cookie> result = cookieRetriever.getCookie(request, requestedCookieName);
        assertEquals(Optional.empty(), result);
    }
}
