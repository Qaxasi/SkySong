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
import java.util.Optional;

import static com.mycompany.SkySong.testsupport.auth.security.CookieDeleterImplTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;


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
        Cookie cookie = new Cookie("cookie", "value");
        request.setCookies(cookie);

        deleter.deleteCookie(request, response, "cookie");

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
