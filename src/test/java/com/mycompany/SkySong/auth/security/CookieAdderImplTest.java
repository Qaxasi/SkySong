package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.testsupport.auth.security.CookieAdderImplTestHelper;
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
    void whenCreateCookie_HaveCorrectProperties() {
        Cookie cookie = CookieAdderImplTestHelper.addCookie(
                cookieAdder, response, "cookie", "value", 3600);

        CookieAdderImplTestHelper.assertCookieProperties(
                cookie, "cookie", "value", 3600,
                "/", true, true);
    }
    @Test
    void shouldHandleNullResponse() {
        assertThrows(NullPointerException.class, () -> cookieAdder.addCookie(
                null, "testCookie", "testValue",  3600));
    }
}
