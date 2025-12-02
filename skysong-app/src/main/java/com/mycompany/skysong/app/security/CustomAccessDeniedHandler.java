package com.mycompany.skysong.app.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.skysong.web.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    public CustomAccessDeniedHandler(final ObjectMapper mapper) {
        this.objectMapper = mapper;
    }
    @Override
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AccessDeniedException accessDeniedException) throws IOException {

        final ErrorResponse body = new ErrorResponse(
                "You do not have permission to perform this operation",
                "ACCESS_DENIED",
                HttpStatus.FORBIDDEN.value(),
                Map.of());

        response.setStatus(body.status());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), body);
    }
}
