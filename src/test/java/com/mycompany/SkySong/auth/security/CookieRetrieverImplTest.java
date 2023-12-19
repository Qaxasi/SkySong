package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.mycompany.SkySong.testsupport.auth.security.CookieRetrieverImplTestHelper.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CookieRetrieverImplTest {
    @InjectMocks
    private CookieRetrieverImpl cookieRetriever;
    @Mock
    private HttpServletRequest request;

    @Test
    void whenNoCookieRequest_ReturnEmptyOptional() {
        assertNoCookiesReturnEmptyOptional(cookieRetriever, request, "cookie");
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
    @Test
    void whenRequestNull_ThrowException() {
        assertThrows(NullPointerException.class, () -> cookieRetriever.getCookie(null, "testCookie"));
    }
}
