package com.mycompany.SkySong.shared.error;

import org.springframework.http.HttpStatus;

public enum ErrorType {

    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY),
    GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT),
    TO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS);

    private final HttpStatus httpStatus;
    ErrorType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
