package com.mycompany.SkySong.adapter.exception.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ErrorResponseBuilder {

    private ErrorResponseBuilder() {
    }

    public static ResponseEntity<Object> createErrorResponse(String errorMessage, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return new ResponseEntity<>(errorResponse, status);
    }
    public static ResponseEntity<Object> createErrorResponse(Map<String, String> errors, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(errors);
        return new ResponseEntity<>(errorResponse, status);
    }
}
