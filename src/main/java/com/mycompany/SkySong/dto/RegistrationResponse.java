package com.mycompany.SkySong.dto;

public class RegistrationResponse {
    private final boolean success;
    private final String message;

    public RegistrationResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
