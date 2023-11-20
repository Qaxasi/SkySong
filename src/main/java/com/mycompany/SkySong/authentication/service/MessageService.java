package com.mycompany.SkySong.authentication.service;

public interface MessageService {
    String getMessage(String key);
    String getMessage(String key, Object...params);
}
