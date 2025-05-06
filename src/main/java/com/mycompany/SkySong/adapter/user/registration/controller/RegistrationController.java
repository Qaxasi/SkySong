package com.mycompany.SkySong.adapter.user.registration.controller;

import com.mycompany.SkySong.adapter.user.registration.mapper.RegistrationRequestMapper;
import com.mycompany.SkySong.adapter.user.registration.dto.RegistrationRequest;
import com.mycompany.SkySong.application.user.registration.dto.UserRegistrationDto;
import com.mycompany.SkySong.application.user.registration.usecase.UserRegistration;
import com.mycompany.SkySong.shared.response.ApiResponse;
import com.mycompany.SkySong.shared.result.Result;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class RegistrationController {

    private final UserRegistration registration;
    private final RegistrationRequestMapper mapper;

    public RegistrationController(final UserRegistration registration,
                                  final RegistrationRequestMapper mapper) {
        this.registration = registration;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody final RegistrationRequest request) {
        final UserRegistrationDto dto = mapper.toDto(request);
        final Result<ApiResponse> result = registration.registerUser(dto);

        if (result.isFailure()) {
            return ResponseEntity.status(result.errorType().getHttpStatus())
                    .body(result.toErrorResponse());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result.data());
    }
}
