package com.mycompany.SkySong.identity.adapter.in.web.authentication;

import com.mycompany.skysong.web.error.ErrorResponse;
import com.mycompany.skysong.web.response.ResponsePayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

final class LoginErrorResponses {
    private LoginErrorResponses() {}
    static ResponseEntity<ResponsePayload<AuthResponse>> unauthorizedErrorResponse() {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;
        final ErrorResponse body = new ErrorResponse(
                "Invalid login credentials",
                status.name(),
                status.value(),
                Map.of());

        return ResponseEntity
                .status(status)
                .body(ResponsePayload.error(body));
    }

    static ResponseEntity<ResponsePayload<AuthResponse>> internalErrorResponse() {
        final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        final ErrorResponse body = new ErrorResponse(
                "We encountered an issue processing your login. Please try again later.",
                status.name(),
                status.value(),
                Map.of()
                );

        return ResponseEntity
                .status(status)
                .body(ResponsePayload.error(body));
    }
}
