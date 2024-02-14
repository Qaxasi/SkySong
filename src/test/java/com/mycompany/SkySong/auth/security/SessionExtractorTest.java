package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionExtractorTest {

    private SessionExtractor sessionExtractor;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        sessionExtractor = new SessionExtractorImpl();
    }

    @Test
    void whenSessionIdCookieExist_ReturnSessionId() {
        Cookie cookie = new Cookie("session_id", "value");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        String sessionId = sessionExtractor.getSessionIdFromRequest(request);

        assertThat(sessionId).isEqualTo("value");
    }

    @Test
    void whenCookieNotExist_ReturnNull() {
        when(request.getCookies()).thenReturn(null);

        String sessionId = sessionExtractor.getSessionIdFromRequest(request);

        assertThat(sessionId).isNull();
    }
}
