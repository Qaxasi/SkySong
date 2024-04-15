package com.mycompany.SkySong.common.utils;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

@Service
public class ApplicationMessageLoader {

    private final Properties messages = new Properties();

    public ApplicationMessageLoader() {
        this(ApplicationMessageLoader.class.getClassLoader().getResourceAsStream("messages.properties"));
    }

    ApplicationMessageLoader(InputStream propertiesStream) {
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
