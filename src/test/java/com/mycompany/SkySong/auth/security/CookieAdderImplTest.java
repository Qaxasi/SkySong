package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.mycompany.SkySong.testsupport.auth.security.CookieAdderImplTestHelper.addCookie;
import static com.mycompany.SkySong.testsupport.auth.security.CookieAdderImplTestHelper.assertCookieProperties;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CookieAdderImplTest {
    @InjectMocks
    private CookieAdderImpl cookieAdder;
    @Mock
    private HttpServletResponse response;

    @Test
    void whenCreateCookie_HaveCorrectProperties() {
        Cookie cookie = addCookie(cookieAdder, response, "cookie", "value", 3600);
        assertCookieProperties(
                cookie, "cookie", "value", 3600,
                "/", true, true);
    }
    @Test
    void whenResponseNull_HandleException() {
        assertThrows(NullPointerException.class, () -> addCookie(
                cookieAdder, null, "testCookie", "testValue",  3600));
    }
}
