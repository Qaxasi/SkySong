package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.mycompany.SkySong.testsupport.auth.security.CookieRetrieverImplTestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

public class CookieRetrieverImplTest {

    private CookieRetrieverImpl cookieRetriever;

    private HttpServletRequest request;

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
