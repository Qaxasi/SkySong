package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.dto.JWTAuthResponse;
import com.mycompany.SkySong.authentication.dto.RegistrationResponse;
import com.mycompany.SkySong.authentication.service.AuthService;
import com.mycompany.SkySong.authentication.dto.LoginRequest;
import com.mycompany.SkySong.authentication.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegistrationResponse registrationResponse = authService.register(registerRequest);
        return new ResponseEntity<>(registrationResponse, HttpStatus.CREATED);
    }
}
