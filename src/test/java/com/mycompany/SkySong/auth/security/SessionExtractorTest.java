package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SessionExtractorTest {

    private SessionExtractor sessionExtractor;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        sessionExtractor = new SessionExtractorImpl();
    }

}
