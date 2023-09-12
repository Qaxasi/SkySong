package com.mycompany.SkySong.exception;

public class ServerIsUnavailable extends RuntimeException{
    public ServerIsUnavailable(String message) {
        super(message);
    }
    public ServerIsUnavailable(String message, Throwable cause) {
        super(message, cause);
    }
}
