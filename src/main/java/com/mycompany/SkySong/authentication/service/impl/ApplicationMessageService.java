package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.service.MessageService;

import java.util.Properties;

public class ApplicationMessageService implements MessageService {
    private final Properties messages = new Properties();
    @Override
    public String getMessage(String key) {
        return null;
    }

    @Override
    public String getMessage(String key, Object... params) {
        return null;
    }
}
