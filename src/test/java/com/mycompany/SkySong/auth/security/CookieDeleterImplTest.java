package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.mycompany.SkySong.testsupport.auth.security.CookieDeleterImplTestHelper.*;

@ExtendWith(MockitoExtension.class)
public class CookieDeleterImplTest {
    @InjectMocks
    private CookieDeleterImpl cookieDeleter;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Test
    void whenCookiePresent_Delete() {
        Cookie cookie = new Cookie("testCookie", "testValue");
        deleteAndGetCookie(cookieDeleter, request, response, "testCookie", cookie);
        assertDeletedCookie(cookie, "testCookie");
    }
}
