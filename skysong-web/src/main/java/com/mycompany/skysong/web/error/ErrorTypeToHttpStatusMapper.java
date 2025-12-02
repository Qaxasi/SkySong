package com.mycompany.skysong.web.error;

import com.mycompany.skysong.core.error.ErrorType;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class HttpErrorMapper {

    private HttpErrorMapper() {
    }

    public static ErrorResponse toResponse(final String message,
                                           final ErrorType errorType) {
        return toResponse(message, errorType, Map.of());
    }

    public static ErrorResponse toResponse(final String message,
                                           final ErrorType errorType,
                                           final Map<String, String> errors) {

        final HttpStatus status = toHttpStatus(errorType);
        return new ErrorResponse(
                message,
                errorType.name(),
                status.value(),
                errors != null ? errors : Map.of());
    }

    public static HttpStatus toHttpStatus(final ErrorType errorType) {
        return switch (errorType) {
            case INVALID_LOGIN_CREDENTIALS,
                    ACCOUNT_LOCKED,
                    ACCOUNT_DISABLED,
                    INVALID_REFRESH_TOKEN,
                    INVALID_SESSION-> HttpStatus.UNAUTHORIZED;

            case VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;

            case AUTHENTICATION_SERVICE_ERROR,
                    AUTHENTICATION_FAILED,
                    ACCESS_SNAPSHOT_NOT_FOUND,
                    PERSISTENCE_ERROR,
                    INVARIANT_VIOLATION,
                    INTERNAL_ERROR,
                    SERIALIZATION_ERROR,
                    DESERIALIZATION_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;

            default -> HttpStatus.INTERNAL_SERVER_ERROR;

        };
    }
}

