package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.CookieRetriever;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

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
}
