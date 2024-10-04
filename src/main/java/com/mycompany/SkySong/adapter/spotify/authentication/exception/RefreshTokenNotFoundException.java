package com.mycompany.SkySong.adapter.spotify.authentication.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
    public RefreshTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
