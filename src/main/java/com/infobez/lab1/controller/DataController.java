package com.infobez.lab1.controller;

import com.infobez.lab1.dto.UserResponse;
import com.infobez.lab1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data")
@RequiredArgsConstructor
public class DataController {
    
    private final UserService userService;
    
    /**
     * GET /api/data - Получение списка всех пользователей
     * Доступно только аутентифицированным пользователям
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET /api/data/{id} - Получение пользователя по ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    /**
     * GET /api/data/username/{username} - Получение пользователя по username
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}