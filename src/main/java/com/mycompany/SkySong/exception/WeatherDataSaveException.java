package com.mycompany.SkySong.exception;

public class WeatherDataSaveException extends RuntimeException {
    public WeatherDataSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
