package com.mycompany.SkySong.adapter.exception.handler;

import com.mycompany.SkySong.adapter.exception.common.*;
import com.mycompany.SkySong.adapter.exception.response.ErrorResponseBuilder;;
import com.mycompany.SkySong.adapter.exception.common.ApiClientErrorException;
import com.mycompany.SkySong.adapter.exception.common.ApiServerErrorException;
import com.mycompany.SkySong.adapter.user.delete.persistence.exception.UserNotFoundException;
import com.mycompany.SkySong.domain.registration.exception.CredentialValidationException;
import com.mycompany.SkySong.adapter.user.delete.controller.exception.NullOrEmptyInputException;
import com.mycompany.SkySong.domain.registration.exception.RoleNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("Unexpected error: " + ex.getMessage());
        return ErrorResponseBuilder.createErrorResponse("Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiAccessDeniedException.class)
    public ResponseEntity<Object> handleApiAccessDeniedException(final ApiAccessDeniedException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ApiClientErrorException.class)
    public ResponseEntity<Object> handleTokenRequestClientException(final ApiClientErrorException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiServerErrorException.class)
    public ResponseEntity<Object> handleTokenRequestServerException(final ApiServerErrorException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApiRequestTimeoutException.class)
    public ResponseEntity<Object> handleWebClientTimeoutException(final ApiRequestTimeoutException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(TooManyListenersException.class)
    public ResponseEntity<Object> handleTooManyListenersException(final TooManyRequestsException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(ApiAuthenticationException.class)
    public ResponseEntity<Object> handleAuthorizationException(final ApiAuthenticationException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Object> handleServerIsUnavailableException(final ServiceUnavailableException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }


    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<Object> handleWebClientException(final InternalServerErrorException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFoundException(final DataNotFoundException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handlerRoleNotFoundException(final RoleNotFoundException ex) {
        log.error("A problem occurred while assigning a default role to the user.");
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handlerUserNotFoundException(final UserNotFoundException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NullOrEmptyInputException.class, CredentialValidationException.class})
    public ResponseEntity<Object> handleBadRequestExceptions(final RuntimeException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Object> handleUnauthorizedExceptions(final RuntimeException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException() {
        String message = "Invalid input data format.";
        return ErrorResponseBuilder.createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ErrorResponseBuilder.createErrorResponse(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        String errorMessage = "Invalid JSON format: " + exception.getMessage();
        return ErrorResponseBuilder.createErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
