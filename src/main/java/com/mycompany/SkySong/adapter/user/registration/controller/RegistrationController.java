package com.mycompany.SkySong.adapter.registration.controller;

import com.mycompany.SkySong.adapter.registration.mapper.RegisterRequestMapper;
import com.mycompany.SkySong.application.registration.dto.UserRegistrationDto;
import com.mycompany.SkySong.application.registration.usecase.UserRegistrationHandler;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.adapter.registration.dto.RegisterRequest;
import com.mycompany.SkySong.shared.result.Result;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class RegistrationController {

    private final UserRegistrationHandler registration;
    private final RegisterRequestMapper mapper;

    public RegistrationController(UserRegistrationHandler registration,
                                  RegisterRequestMapper mapper) {
        this.registration = registration;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request) {
        UserRegistrationDto dto = mapper.toDto(request);
        Result<ApiResponse> result = registration.registerUser(dto);

        if (result.isFailure()) {
            return ResponseEntity.status(result.errorType().getHttpStatus())
                    .body(result.toErrorResponse());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.data());
    }
}
