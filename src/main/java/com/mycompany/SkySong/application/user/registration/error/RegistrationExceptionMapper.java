package com.mycompany.SkySong.application.user.registration.error;

import com.mycompany.SkySong.domain.shared.exception.DomainException;
import com.mycompany.SkySong.domain.user.registration.exception.*;
import com.mycompany.SkySong.shared.error.ErrorType;

import java.util.Map;

public class RegistrationExceptionMapper {
    private static final Map<Class<? extends DomainException>, ErrorType> ERROR_MAP = Map.of(
            InvalidUsernameFormat.class, ErrorType.INVALID_USERNAME_FORMAT,
            InvalidEmailFormat.class,    ErrorType.INVALID_EMAIL_FORMAT,
            InvalidPasswordFormat.class, ErrorType.INVALID_PASSWORD_FORMAT,
            UsernameAlreadyExist.class, ErrorType.USERNAME_EXIST,
            EmailAlreadyExist.class,    ErrorType.EMAIL_EXIST
    );

    private RegistrationExceptionMapper() {}

    public static ErrorType map(DomainException ex) {
        return ERROR_MAP.getOrDefault(ex.getClass(), ErrorType.INTERNAL_SERVER_ERROR);
    }
}