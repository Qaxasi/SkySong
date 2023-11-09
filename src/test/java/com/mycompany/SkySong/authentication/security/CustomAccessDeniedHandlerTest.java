package com.mycompany.SkySong.authentication.security;

import com.mycompany.SkySong.authentication.secutiry.CustomAccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomAccessDeniedHandlerTest {
    private CustomAccessDeniedHandler customAccessDeniedHandler;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AccessDeniedException exception;
    private StringWriter stringWriter;
    private PrintWriter printWriter;
    @BeforeEach
    void setUp() {
        customAccessDeniedHandler = new CustomAccessDeniedHandler();

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }
    @Test
    void shouldReturnMessageWhenHandlingAccessDenied() throws IOException {
        when(response.getWriter()).thenReturn(printWriter);

        customAccessDeniedHandler.handle(request, response, exception);

        String jsonResponse = stringWriter.toString();

        JSONObject jsonObject = new JSONObject(jsonResponse);

        assertEquals("You do not have permission to perform this operation.",
                jsonObject.getString("error"));
    }

}
