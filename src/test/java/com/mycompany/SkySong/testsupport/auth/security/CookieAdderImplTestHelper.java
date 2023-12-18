package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.CookieAdder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.verify;

public class CookieAdderImplTestHelper {
    public static Cookie addCookie(CookieAdder cookieAdder, HttpServletResponse response, String name,
                                   String value, int maxAge) {
        cookieAdder.addCookie(response, name, value, maxAge);

        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        return cookieCaptor.getValue();
    }
}
