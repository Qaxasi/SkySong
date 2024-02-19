package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.auth.exception.DatabaseException;
import com.mycompany.SkySong.auth.model.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;

public interface RegistrationService {
    ApiResponse register(RegisterRequest registerRequest) throws DatabaseException;
}
