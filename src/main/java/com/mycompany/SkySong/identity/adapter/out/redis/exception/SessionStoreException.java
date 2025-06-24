package com.mycompany.SkySong.identity.adapter.out.redis.exception;

public class SessionStoreException extends RuntimeException {
    public SessionStoreException(String message, Throwable ex) {
        super(message, ex);
    }
}
