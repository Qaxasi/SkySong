package com.mycompany.SkySong.registration.domain.ports;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;

public interface UserRegistration {
    ApiResponse registerUser(RegisterRequest request);
}
