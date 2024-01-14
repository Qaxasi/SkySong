package com.mycompany.SkySong.testsupport.auth.security;

import jakarta.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieAdderTestHelper {
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
