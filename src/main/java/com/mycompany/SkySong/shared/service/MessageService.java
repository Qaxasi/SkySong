package com.mycompany.SkySong.shared.service;

public interface MessageService {
    String getMessage(String key);
    String getMessage(String key, Object...params);
}
