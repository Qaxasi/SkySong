package com.mycompany.SkySong.authentication.service.impl;

import com.mycompany.SkySong.authentication.service.MessageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;
@Service
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
        return messages.getProperty(key, "Default message for " + key);
    }

    @Override
    public String getMessage(String key, Object... params) {
        String message = messages.getProperty(key, "Default message for " + key);
        return MessageFormat.format(message, params);
    }
}
