package com.mycompany.skysong.identity.adapter.in.web.authentication;

import com.mycompany.skysong.web.error.ErrorResponse;
import com.mycompany.skysong.web.response.ResponsePayload;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

final class RefreshSessionErrorResponses {

    private RefreshSessionErrorResponses() {}

    static ResponseEntity<ResponsePayload<AuthResponse>> unauthorizedErrorResponse() {
        final HttpStatus status = HttpStatus.UNAUTHORIZED;

        final ErrorResponse body = new ErrorResponse(
                "Invalid or expired session. Please log in again",
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
                "We encountered an issue refreshing your session. Please try again later",
                status.name(),
                status.value(),
                Map.of());

        return ResponseEntity
                .status(status)
                .body(ResponsePayload.error(body));
    }
}
