package com.mycompany.SkySong.auth.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

import static com.mycompany.SkySong.testsupport.auth.security.AccessDeniedTestHelper.assertAccessDeniedMessage;

@ExtendWith(MockitoExtension.class)
public class CustomAccessDeniedHandlerTest {
    @InjectMocks
    private CustomAccessDeniedHandler handler;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AccessDeniedException exception;

    @Test
    void whenHandleException_ReturnMessage() throws IOException {
        assertAccessDeniedMessage(handler, request, response, exception,
                "You do not have permission to perform this operation.");
    }
}
