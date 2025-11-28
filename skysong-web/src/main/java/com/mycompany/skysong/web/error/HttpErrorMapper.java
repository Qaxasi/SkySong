package com.mycompany.skysong.web.error;

import com.mycompany.skysong.web.response.ResponsePayloadss;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Component
public class HttpErrorMapper {
    public ResponseEntity<ResponsePayloadss<AuthResponse>> mapLoginFailure(final Failure<?> f) {
        final HttpStatus status = f.errorType().getHttpStatus();

        if (status.is4xxClientError()) {
            return failure("Invalid login credentials", ErrorType.INVALID_LOGIN_CREDENTIALS);
        }
        return failure("Internal service error", f.errorType());
    }
    public ResponseEntity<ResponsePayloadss<AuthResponse>> mapRefreshSessionFailure(final Failure<?> f) {
        final HttpStatus status = f.errorType().getHttpStatus();
        if (status.is4xxClientError()) {
            return switch (f.errorType()) {
                case SESSION_NOT_FOUND, VALIDATION_ERROR, INVALID_SESSION  ->
                        failure("Session expired, please log in again", ErrorType.SESSION_NOT_FOUND);
                case CONFLICT ->
                        failure("Request conflict. Please retry", ErrorType.CONFLICT);
                default ->
                        failure("Couldn't process your request", f.errorType());
            };
        }
        return failure("Internal service error", f.errorType());
    }

    public <T> ResponseEntity<ResponsePayloadss<T>> failure(final String message, final ErrorType errorType) {
        final HttpStatus status = errorType.getHttpStatus();
        final ErrorResponse errorResponse = new ErrorResponse(message, errorType.name(), status.value(), null);

        return ResponseEntity.status(status)
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(ResponsePayloadss.error(errorResponse));
    }


    public ResponseEntity<ErrorResponse> failure(final ErrorType errorType, final Map<String, String> fieldErrors) {
        final HttpStatus status = errorType.getHttpStatus();
        final Map<String, String> errors = (fieldErrors == null || fieldErrors.isEmpty())
                ? null
                : Map.copyOf(fieldErrors);

        return ResponseEntity.status(status)
                .body(new ErrorResponse(null, errorType.name(), status.value(), errors));
    }

    public ResponseEntity<ErrorResponse> from(final ErrorView view) {
        final HttpStatus status = view.errorType().getHttpStatus();
        final String message = messageForError(view.errorType(), view.message());

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(message, view.errorType().name(), status.value()));

    }

    public ResponseEntity<ErrorResponse> from(final Result<?> failure) {
        final HttpStatus status = failure.errorType().getHttpStatus();
        final String message = messageForError(failure.errorType(), failure.errorMessage());

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(message, failure.errorType().name(), status.value()));

    }

    private String messageForError(final ErrorType errorType, final String message) {
        final HttpStatus status = errorType.getHttpStatus();

        switch (errorType) {
            case VALIDATION_ERROR: return "Invalid request body";
            case AUTHENTICATION_FAILED: return "Invalid login credentials";
            case INVALID_REFRESH_TOKEN: return "Invalid refresh token";
            case SESSION_REVOKED: return "Invalid token";
            case ACCOUNT_RESTRICTED: return "Account is not allowed to sign in";
            case SERVICE_UNAVAILABLE: return "Service temporarily unavailable";
            case INTERNAL_ERROR: return "Internal server error";
            default:
        }

        if (status.is5xxServerError()) {
            return "Internal server error";
        }

        if (status.is4xxClientError()) {
            return "Bad request";
        }

        return (message == null || message.isBlank())
                ? "Request could not be processed"
                : message;
    }
}
//@Component
//public class HttpErrorMapper {
//
//    private final MessageSource messages; // opcjonalnie
//    private final Clock clock;            // opcjonalnie do timestampów
//
//    public HttpErrorMapper(MessageSource messages, Clock clock) {
//        this.messages = messages;
//        this.clock = clock;
//    }
//
//    public ResponseEntity<BaseResponse> from(Result<?> failure) {
//        ErrorType type = failure.getErrorType();
//        int status = statusOf(type);
//        String msg = resolveMessage(type, failure.getErrorMessage());
//        return ResponseEntity.status(status).body(
//                new ErrorResponse(msg, type, status, traceId(), Instant.now(clock), null)
//        );
//    }
//
//    public ResponseEntity<BaseResponse> from(ErrorType type, String message) {
//        int status = statusOf(type);
//        String msg = resolveMessage(type, message);
//        return ResponseEntity.status(status).body(
//                new ErrorResponse(msg, type, status, traceId(), Instant.now(clock), null)
//        );
//    }
//
//    public ResponseEntity<BaseResponse> from(ErrorType type, String message, Map<String,String> fieldErrors) {
//        int status = statusOf(type);
//        String msg = resolveMessage(type, message);
//        return ResponseEntity.status(status).body(
//                new ErrorResponse(msg, type, status, traceId(), Instant.now(clock), fieldErrors)
//        );
//    }
//
//    private int statusOf(ErrorType t) {
//        return switch (t) {
//            case INVALID_LOGIN_CREDENTIALS, UNAUTHORIZED -> 401;
//            case FORBIDDEN -> 403;
//            case NOT_FOUND -> 404;
//            case VALIDATION_ERROR -> 422;
//            case ACCOUNT_LOCKED -> 423;
//            case RATE_LIMITED, TOO_MANY_ATTEMPTS -> 429;
//            case SERVICE_UNAVAILABLE -> 503;
//            default -> 500;
//        };
//    }
//
//    private String resolveMessage(ErrorType type, String fallback) {
//        Locale locale = LocaleContextHolder.getLocale();
//        try { return messages.getMessage("errors." + type.name().toLowerCase(), null, locale); }
//        catch (NoSuchMessageException e) { return fallback != null ? fallback : type.name(); }
//    }
//
//    private String traceId() { return MDC.get("traceId"); } // lub z innego miejsca
//}
//
