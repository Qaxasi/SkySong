package com.mycompany.SkySong.authentication.controller;

import com.mycompany.SkySong.authentication.model.dto.JWTAuthResponse;
import com.mycompany.SkySong.authentication.model.dto.LoginRequest;
import com.mycompany.SkySong.authentication.model.dto.RegisterRequest;
import com.mycompany.SkySong.authentication.model.dto.RegistrationResponse;
import com.mycompany.SkySong.authentication.service.LoginService;
import com.mycompany.SkySong.authentication.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class AuthController {
    private final LoginService loginService;
    private final RegistrationService registrationService;
    public AuthController(LoginService loginService, RegistrationService registrationService) {
        this.loginService = loginService;
        this.registrationService = registrationService;
    }

    @PostMapping("/login")
    public ResponseEntity<JWTAuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = loginService.login(loginRequest);
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse(token);
        return ResponseEntity.ok(jwtAuthResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegistrationResponse registrationResponse = registrationService.register(registerRequest);
        return new ResponseEntity<>(registrationResponse, HttpStatus.CREATED);
    }
}
