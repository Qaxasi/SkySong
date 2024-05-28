package com.mycompany.SkySong.registration.adapters;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.registration.domain.ports.UserRegistration;

public class TransactionUserRegistration implements UserRegistration {
    @Override
    public ApiResponse registerUser(RegisterRequest request) {
        return null;
    }
}
