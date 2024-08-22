package com.mycompany.SkySong.adapter.registration.controller;

import com.mycompany.SkySong.application.registration.usecase.UserRegistrationHandler;
import com.mycompany.SkySong.application.shared.dto.ApiResponse;
import com.mycompany.SkySong.application.registration.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class RegistrationController {

    private final UserRegistrationHandler registration;

    public RegistrationController(UserRegistrationHandler registration) {
        this.registration = registration;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        ApiResponse registrationResponse = registration.registerUser(request);
        return new ResponseEntity<>(registrationResponse, HttpStatus.CREATED);
    }
}
