package com.mycompany.SkySong.controller;

import com.mycompany.SkySong.dto.JWTAuthResponse;
import com.mycompany.SkySong.dto.RegistrationResponse;
import com.mycompany.SkySong.service.AuthService;
import com.mycompany.SkySong.dto.LoginRequest;
import com.mycompany.SkySong.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegistrationResponse registrationResponse = authService.register(registerRequest);
        return new ResponseEntity<>(registrationResponse, HttpStatus.CREATED);
    }
}
