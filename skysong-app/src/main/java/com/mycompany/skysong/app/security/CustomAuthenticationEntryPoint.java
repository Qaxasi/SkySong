package com.mycompany.skysong.app.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.skysong.app.security.exception.TokenExpiredException;
import com.mycompany.skysong.web.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    public CustomAuthenticationEntryPoint(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        final ErrorResponse body;
        if (authException instanceof TokenExpiredException) {
            body = new ErrorResponse(
                    "Your session has expired. Please refresh your token to continue",
                    "SESSION_EXPIRED",
                    HttpStatus.UNAUTHORIZED.value(),
                    Map.of());

        } else {
            body = new ErrorResponse(
                    "Unauthorized access. Please log in.",
                    "ACCESS_DENIED",
                    HttpStatus.UNAUTHORIZED.value(),
                    Map.of());

        }
        response.setStatus(body.status());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), body);
    }
}