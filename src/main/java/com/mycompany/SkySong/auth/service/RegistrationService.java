package com.mycompany.SkySong.auth.service;

import com.mycompany.SkySong.shared.exception.DatabaseException;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.auth.model.dto.RegisterRequest;

public interface RegistrationService {
    ApiResponse register(RegisterRequest registerRequest) throws DatabaseException;
}
