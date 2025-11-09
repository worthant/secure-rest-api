package com.infobez.lab1.controller;

import com.infobez.lab1.dto.PostRequest;
import com.infobez.lab1.dto.PostResponse;
import com.infobez.lab1.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    
    private final PostService postService;
    
    /**
     * POST /api/posts - Создание нового поста
     * Доступно только аутентифицированным пользователям
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest) {
        try {
            PostResponse post = postService.createPost(postRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(post);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create post: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    /**
     * GET /api/posts - Получение всех постов
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }
    
    /**
     * GET /api/posts/{id} - Получение поста по ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            PostResponse post = postService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    /**
     * GET /api/posts/user/{username} - Получение всех постов пользователя
     */
    @GetMapping("/user/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getPostsByUser(@PathVariable String username) {
        try {
            List<PostResponse> posts = postService.getPostsByUser(username);
            return ResponseEntity.ok(posts);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}