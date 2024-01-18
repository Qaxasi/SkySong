package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Arrays;

import static com.mycompany.SkySong.testsupport.auth.security.CookieAdderTestHelper.assertCookieProperties;
import static org.junit.jupiter.api.Assertions.*;

public class CookieAdderImplTest {
    private CookieAdderImpl adder;
    private MockHttpServletResponse response;
    @BeforeEach
    void setUp() {
        adder = new CookieAdderImpl();
        response = new MockHttpServletResponse();
    }

    @Test
    void whenCreateCookie_HaveCorrectProperties() {
        adder.addCookie(response, "cookie", "value", 3600);

        Cookie cookie = getCookie(response, "cookie");

        assertCookieProperties(cookie, "cookie", "value",
                3600, "/", true, true);
    }
    private Cookie getCookie(MockHttpServletResponse response, String name) {
        Cookie[] cookies = response.getCookies();
        assertNotNull(cookies);
        return Arrays.stream(cookies)
                .filter(c -> name.equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Cookie not found " + name));
    }
}
