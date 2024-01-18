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

import java.util.Optional;

import static com.mycompany.SkySong.testsupport.auth.security.CookieRetrieverImplTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CookieRetrieverImplTest {

    private CookieRetrieverImpl retriever;

    private MockHttpServletRequest request;
    @BeforeEach
    void setUp() {
        retriever = new CookieRetrieverImpl();
        request = new MockHttpServletRequest();
    }

    @Test
    void whenNoCookieInRequest_ReturnOptionalEmpty() {
        //when
        Optional<Cookie> retrievedCookie = retriever.getCookie(request, "cookie");

        //then
        assertThat(retrievedCookie).isEmpty();
    }
    @Test
    void whenCookiePresentInRequest_ReturnCookie() {
        //given
        Cookie cookie = new Cookie("cookie", "value");
        request.setCookies(cookie);

        //when
        Optional<Cookie> retrievedCookie = retriever.getCookie(request, "cookie");

        //then
        assertThat(retrievedCookie.isPresent()).isTrue();
        assertThat(retrievedCookie.get()).isEqualTo(cookie);
    }
}
