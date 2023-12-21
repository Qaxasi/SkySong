package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.JwtAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class JwtAuthenticationEntryPointTestHelper {
    public static void assertAuthenticationMessage(JwtAuthenticationEntryPoint authenticationEntryPoint,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response,
                                                   AuthenticationException exception,
                                                   String expectedMessage) throws IOException {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        authenticationEntryPoint.commence(request, response, exception);

        printWriter.flush();
        String jsonResponse = stringWriter.toString();

        JSONObject jsonObject = new JSONObject(jsonResponse);

        assertEquals(expectedMessage, jsonObject.getString("error"));
    }
}
