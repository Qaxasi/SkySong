package com.mycompany.SkySong.auth.service;

public interface ApplicationMessageService {
    String getMessage(String key);
    String getMessage(String key, Object...params);
}
