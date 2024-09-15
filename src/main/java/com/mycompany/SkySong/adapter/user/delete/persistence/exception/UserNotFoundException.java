package com.mycompany.SkySong.adapter.user.delete.persistence.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
