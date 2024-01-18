package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
    void whenCookiePresent_AssertProperDeletion() {
        //given
        Cookie cookie = new Cookie("cookie", "value");
        request.setCookies(cookie);

        //when
        deleter.deleteCookie(request, response, "cookie");

        //then
        Optional<Cookie> optionalCookie = Arrays.stream(response.getCookies())
                .filter(c -> "cookie".equals(c.getName()))
                .findFirst();

        optionalCookie.ifPresent(deletedCookie -> {

            assertThat(deletedCookie.getName()).isEqualTo("cookie");

            assertThat(deletedCookie.getMaxAge()).isEqualTo(0);

            assertThat(deletedCookie.getValue()).isNull();
        });
    }
}
