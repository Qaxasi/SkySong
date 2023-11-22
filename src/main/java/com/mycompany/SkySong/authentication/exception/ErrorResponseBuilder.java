package com.mycompany.SkySong.authentication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponseBuilder {
    public static ResponseEntity<Object> createErrorResponse(String errorMessage, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", errorMessage);
        return new ResponseEntity<>(response, status);
    }
}
