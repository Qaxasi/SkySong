package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.CookieDeleter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CookieDeleterImplTestHelper {
    public static Cookie performCookieDeletionAndGetModifiedCookie(CookieDeleter deleter,
                                                                   HttpServletRequest request,
                                                                   HttpServletResponse response,
                                                                   String cookieName,
                                                                   Cookie... cookies) {

        when(request.getCookies()).thenReturn(cookies);

        deleter.deleteCookie(request, response, cookieName);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        return cookieCaptor.getValue();
    }
    public static void assertDeletedCookie(Cookie cookie, String expectedName) {
        assertAll("modifiedCookie",
                () -> assertEquals(expectedName, cookie.getName()),
                () -> assertNull(cookie.getValue()),
                () -> assertEquals(0, cookie.getMaxAge()));
    }
}
