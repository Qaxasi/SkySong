package com.mycompany.SkySong.shared.exception;

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

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("Unexpected error: " + ex.getMessage());
        return ErrorResponseBuilder.createErrorResponse("Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(final UserNotFoundException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({RegisterException.class, NullOrEmptyInputException.class})
    public ResponseEntity<Object> handleBadRequestExceptions(final RuntimeException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({InternalErrorException.class, DatabaseException.class})
    public ResponseEntity<Object> handleInternalServerError(final RuntimeException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler({BadCredentialsException.class, TokenException.class})
    public ResponseEntity<Object> handleUnauthorizedExceptions(final RuntimeException ex) {
        return ErrorResponseBuilder.createErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException() {
        String message = "Invalid input data format";
        return ErrorResponseBuilder.createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ErrorResponseBuilder.createValidationErrorResponse(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        String errorMessage = "Invalid JSON format: " + exception.getMessage();
        return ErrorResponseBuilder.createErrorResponse(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
