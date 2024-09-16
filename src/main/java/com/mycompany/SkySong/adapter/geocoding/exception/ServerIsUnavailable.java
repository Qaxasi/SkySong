package com.mycompany.SkySong.adapter.geocoding.exception;

public class ServerIsUnavailable extends RuntimeException {
    public ServerIsUnavailable(String message) {
        super(message);
    }
}
