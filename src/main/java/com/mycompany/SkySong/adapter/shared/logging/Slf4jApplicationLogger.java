package com.mycompany.SkySong.adapter.shared.logging;

import com.mycompany.SkySong.application.shared.logging.ApplicationLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Slf4jApplicationLogger implements ApplicationLogger {

    @Override
    public void debug(String message) {
        log.debug(message);
    }

    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        log.error(message, throwable);
    }

    @Override
    public void debug(String message, Context context) {
        if (log.isDebugEnabled()) {
            log.debug(appendContext(message, context));
        }
    }

    @Override
    public void info(String message, Context context) {
        if (log.isInfoEnabled()) {
            log.info(appendContext(message, context));
        }
    }

    @Override
    public void warn(String message, Context context) {
        if (log.isWarnEnabled()) {
            log.warn(message, context);
        }
    }

    @Override
    public void error(String message, Throwable throwable, Context context) {
        if (log.isErrorEnabled()) {
            log.error(appendContext(message, context), throwable);
        }
    }

    private String appendContext(String message, Context context) {
        if (context == null || context.isEmpty()) {
            return message;
        }

        return message + " " + context.toFormattedString();
    }
}
