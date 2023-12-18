package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.CookieAdder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.verify;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieAdderImplTestHelper {
    public static Cookie addCookie(CookieAdder cookieAdder, HttpServletResponse response, String name,
                                   String value, int maxAge) {
        cookieAdder.addCookie(response, name, value, maxAge);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        return cookieCaptor.getValue();
    }
    public static void assertCookieProperties(Cookie cookie, String expectedName, String expectedValue,
                                              int expectedMaxAge, String expectedPath, boolean isHttpOnly,
                                              boolean isSecure) {
        assertThat(cookie).isNotNull();
        assertThat(cookie.getName()).isEqualTo(expectedName);
        assertThat(cookie.getValue()).isEqualTo(expectedValue);
        assertThat(cookie.getMaxAge()).isEqualTo(expectedMaxAge);
        assertThat(cookie.getPath()).isEqualTo(expectedPath);
        assertThat(cookie.isHttpOnly()).isEqualTo(isHttpOnly);
        assertThat(cookie.getSecure()).isEqualTo(isSecure);
    }
}
