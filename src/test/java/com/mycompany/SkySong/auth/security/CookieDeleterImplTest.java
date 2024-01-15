package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Arrays;

import static com.mycompany.SkySong.testsupport.auth.security.CookieDeleterImplTestHelper.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CookieDeleterImplTest {
    private CookieDeleterImpl deleter;
    private MockHttpServletRequest request;
    private  MockHttpServletResponse response;
    @BeforeEach
    void setUp() {
        deleter = new CookieDeleterImpl();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }
    @Test
    void whenCookiePresent_Delete() {
        Cookie cookie = new Cookie("testCookie", "testValue");
        deleteAndGetCookie(cookieDeleter, request, response, "testCookie", cookie);
        assertDeletedCookie(cookie, "testCookie");
    }
    private void addCookieToRequest(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        request.setCookies(cookie);
    }
}
