package com.mycompany.SkySong.infrastructure.http;

import com.mycompany.SkySong.shared.error.ErrorType;
import com.mycompany.SkySong.infrastructure.http.response.BaseResponse;
import com.mycompany.SkySong.infrastructure.http.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HttpErrorMapper {

    public ResponseEntity<BaseResponse> from(final ErrorView view) {
        final HttpStatus status = view.errorType().getHttpStatus();
        final String message = messageForError(view.errorType(), view.message());

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(message, view.errorType().name(), status.value()));

    }

    private String messageForError(final ErrorType errorType, String message) {
        final HttpStatus status = errorType.getHttpStatus();
        if (status == HttpStatus.SERVICE_UNAVAILABLE) {
            message = "Service temporarily unavailable";
        } else if (status.is5xxServerError()) {
            message = "Internal server error";
        }
        return (message == null || message.isBlank()) ? "Request could not be processed" : message;
    }
}

// mapować 4xx ???
