package com.mycompany.SkySong.config;

public class ApiErrorBuilder {
    public static ApiError createFromGeneralException(Exception exception) {
        return new ApiError(exception.getMessage());
    }

}
