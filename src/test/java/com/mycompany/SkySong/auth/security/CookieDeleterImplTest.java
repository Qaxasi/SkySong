package com.mycompany.SkySong.auth.security;

import com.mycompany.SkySong.testsupport.auth.security.CookieAdderImplTestHelper;
import com.mycompany.SkySong.testsupport.auth.security.CookieDeleterImplTestHelper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        CookieDeleterImplTestHelper.performCookieDeletionAndGetModifiedCookie(
                cookieDeleter, request, response, "testCookie", cookie);

        CookieDeleterImplTestHelper.assertDeletedCookie(cookie, "testCookie");
    }
    @Test
    void whenCookieNotPresent_ResponseNotModified() {
        Cookie cookie = new Cookie("testCookie", "testValue");
        CookieDeleterImplTestHelper.verifyNoCookieDeletion(
                cookieDeleter,  request, response, "different", cookie);
    }
}
