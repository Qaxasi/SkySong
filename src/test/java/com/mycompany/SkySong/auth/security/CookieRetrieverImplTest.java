package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.mycompany.SkySong.testsupport.auth.security.CookieRetrieverImplTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class CookieRetrieverImplTest {

    private CookieRetrieverImpl retriever;

    private HttpServletRequest request;
    @BeforeEach
    void setUp() {
        retriever = new CookieRetrieverImpl();
        request = new MockHttpServletRequest();
    }

    @Test
    void whenNoCookieInRequest_ReturnOptionalEmpty() {
        Cookie[] cookies = {new Cookie("cookie", "testValue")};
        assertCookieNotInRequestReturnsEmptyOptional(cookieRetriever, request, cookies, "different");
    }
    @Test
    void whenCookiePresentInRequest_ReturnCookie() {
        Cookie expectedCookie = new Cookie("testCookie", "testValue");
        assertCookieRetrievedSuccessfully(cookieRetriever, request, expectedCookie, "testCookie");
    }
}
