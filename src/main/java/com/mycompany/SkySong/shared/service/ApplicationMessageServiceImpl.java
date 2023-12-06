package com.mycompany.SkySong.shared.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;
@Service
class ApplicationMessageServiceImpl implements ApplicationMessageService {
    private final Properties messages = new Properties();

    public ApplicationMessageServiceImpl() {
        this(ApplicationMessageServiceImpl.class.getClassLoader().getResourceAsStream("messages.properties"));
    }
    ApplicationMessageServiceImpl(InputStream propertiesStream) {
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
