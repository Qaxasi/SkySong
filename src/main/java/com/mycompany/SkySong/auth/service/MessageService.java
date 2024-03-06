package com.mycompany.SkySong.auth.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

@Service
public class MessageService {

    private final Properties messages = new Properties();

    public MessageService() {
        this(MessageService.class.getClassLoader().getResourceAsStream("messages.properties"));
    }

    MessageService(InputStream propertiesStream) {
        try {
            if (propertiesStream != null) {
                messages.load(propertiesStream);
            } else {
                throw new IOException("Properties file is null");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load messages.properties");
        }
    }

    public String getMessage(String key) {
        return messages.getProperty(key, "Default message for " + key);
    }

    public String getMessage(String key, Object... params) {
        String message = messages.getProperty(key, "Default message for " + key);
        return MessageFormat.format(message, params);
    }
}
