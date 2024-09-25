package com.mycompany.SkySong.adapter.spotify.exception;

public class TokenRequestClientException extends RuntimeException {
    public TokenRequestClientException(String message) {
        super(message);
    }
    public TokenRequestClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
