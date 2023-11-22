package com.mycompany.SkySong.shared.service;

public interface ApplicationMessageService {
    String getMessage(String key);
    String getMessage(String key, Object...params);
}
