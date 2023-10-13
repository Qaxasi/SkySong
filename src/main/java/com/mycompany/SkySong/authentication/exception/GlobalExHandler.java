package com.mycompany.SkySong.authentication.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExHandler {

    @ExceptionHandler({RegisterException.class, TokenException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleRegisterException(final RegisterException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Validation failed");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String errorMessage = ex.getLocalizedMessage();
        log.info("errorMessage - " + errorMessage);

        boolean isUsernameError = errorMessage.contains("Duplicate entry") && !errorMessage.contains("@");
        boolean isEmailError = errorMessage.contains("Duplicate entry") && errorMessage.contains("@");

        if (isUsernameError) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided username is already taken.");
        } else if (isEmailError) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided email is already taken.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                "The entered data is already in use. Please choose different data.");
    }
}
