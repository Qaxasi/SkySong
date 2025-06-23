package com.mycompany.SkySong.infrastructure.persistence.exception;

public class PersistenceException extends RuntimeException {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
