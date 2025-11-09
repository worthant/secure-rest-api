package com.infobez.lab1.controller;
import com.infobez.lab1.dto.LoginRequest;
import com.infobez.lab1.dto.LoginResponse;
import com.infobez.lab1.dto.RegisterRequest;
import com.infobez.lab1.model.User;
import com.infobez.lab1.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * POST /auth/login - Аутентификация пользователя
     * Принимает логин и пароль, возвращает JWT токен
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    /**
     * POST /auth/register - Регистрация нового пользователя
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            User user = authService.register(registerRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}