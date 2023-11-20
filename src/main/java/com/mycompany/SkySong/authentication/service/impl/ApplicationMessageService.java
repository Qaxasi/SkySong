package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.service.MessageService;

import java.io.IOException;
import java.util.Properties;

public class ApplicationMessageService implements MessageService {
    private final Properties messages = new Properties();

    public ApplicationMessageService() {
        try {
            messages.load(getClass().getClassLoader().getResourceAsStream("messages.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load messages.properties");
        }
    }

    @Override
    public String getMessage(String key) {
        return null;
    }

    @Override
    public String getMessage(String key, Object... params) {
        return null;
    }
}
