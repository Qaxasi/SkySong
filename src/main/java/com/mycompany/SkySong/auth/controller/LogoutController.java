package com.mycompany.SkySong.auth.controller;


import com.mycompany.SkySong.auth.security.SessionDeletion;
import com.mycompany.SkySong.shared.dto.ApiResponse;
import com.mycompany.SkySong.shared.service.ApplicationMessageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class LogoutController {
}