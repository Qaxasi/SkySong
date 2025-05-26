package com.mycompany.SkySong.adapter.redis.session.exception;

public class SessionStoreException extends RuntimeException {
    public SessionStoreException(String message, Throwable ex) {
        super(message, ex);
    }
}
