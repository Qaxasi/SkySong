package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.CookieRetriever;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class CookieRetrieverImplTestHelper {
    public static void assertNoCookiesReturnEmptyOptional(CookieRetriever retriever,
                                                          HttpServletRequest request, String cookieName) {
        when(request.getCookies()).thenReturn(null);
        Optional<Cookie> result = retriever.getCookie(request,  cookieName);
        assertEquals(result, Optional.empty());
    }
    public static void assertCookieNotInRequestReturnsEmptyOptional(CookieRetriever retriever,
                                                                    HttpServletRequest request,
                                                                    Cookie[] cookies,
                                                                    String requestedCookieName) {
        when(request.getCookies()).thenReturn(cookies);
        Optional<Cookie> result = retriever.getCookie(request, requestedCookieName);
        assertEquals(Optional.empty(), result);
    }
    public static void assertCookieRetrievedSuccessfully(CookieRetriever retriever,
                                         HttpServletRequest request,
                                         Cookie expectedCookie,
                                         String cookieName) {

        Cookie[] cookies = {expectedCookie, new Cookie("differentName", "differentValue")};
        when(request.getCookies()).thenReturn(cookies);
        Optional<Cookie> result = retriever.getCookie(request, cookieName);

        assertTrue(result.isPresent());
        assertThat(result.get(), is(expectedCookie));
    }
}
