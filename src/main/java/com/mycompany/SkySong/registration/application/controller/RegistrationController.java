package com.mycompany.SkySong.registration.application.controller;

import com.mycompany.SkySong.common.dto.ApiResponse;
import com.mycompany.SkySong.registration.application.dto.RegisterRequest;
import com.mycompany.SkySong.registration.domain.service.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class RegistrationController {

    private final UserRegistrationService registration;

    public RegistrationController(UserRegistrationService registration) {
        this.registration = registration;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse registrationResponse = registration.register(request);
        return new ResponseEntity<>(registrationResponse, HttpStatus.CREATED);
    }
}
