package com.mycompany.SkySong.testsupport.auth.security;

import com.mycompany.SkySong.auth.security.CustomAccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AccessDeniedTestHelper {
    public static void assertAccessDeniedMessage(CustomAccessDeniedHandler handler,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 AccessDeniedException exception,
                                                 String expectedMessage) throws IOException {

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        handler.handle(request, response, exception);

        String jsonResponse = stringWriter.toString();

        JSONObject jsonObject = new JSONObject(jsonResponse);

        assertEquals(expectedMessage, jsonObject.getString("error"));
    }
}
