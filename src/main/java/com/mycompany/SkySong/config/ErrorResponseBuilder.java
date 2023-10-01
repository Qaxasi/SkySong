package com.mycompany.SkySong.config;

public class ErrorResponseBuilder {
    public static ErrorResponse createFromGeneralException(Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

}