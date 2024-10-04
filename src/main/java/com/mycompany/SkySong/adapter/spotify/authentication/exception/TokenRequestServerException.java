package com.mycompany.SkySong.adapter.spotify.authentication.exception;

public class TokenRequestServerException extends RuntimeException {
    public TokenRequestServerException(String message) {
        super(message);
    }
    public TokenRequestServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
