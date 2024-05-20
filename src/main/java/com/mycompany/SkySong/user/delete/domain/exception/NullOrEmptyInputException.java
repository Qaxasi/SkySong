package com.mycompany.SkySong.user;

import com.mycompany.SkySong.common.exception.ApplicationRuntimeException;

public class NullOrEmptyInputException extends ApplicationRuntimeException {
    public NullOrEmptyInputException(String message) {
        super(message);
    }
}