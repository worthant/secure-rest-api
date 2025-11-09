package com.infobez.lab1.service;
import com.infobez.lab1.dto.LoginRequest;
import com.infobez.lab1.dto.LoginResponse;
import com.infobez.lab1.dto.RegisterRequest;
import com.infobez.lab1.model.User;
import com.infobez.lab1.repository.UserRepository;
import com.infobez.lab1.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    
    @Transactional
    public User register(RegisterRequest request) {
        // Проверяем, существует ли пользователь
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Создаем нового пользователя с хешированным паролем
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // BCrypt хеширование
        user.setEmail(request.getEmail());
        user.setRole("USER");
        
        return userRepository.save(user);
    }
    
    public LoginResponse login(LoginRequest request) {
        // Аутентификация пользователя
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        
        // Загружаем пользователя
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Генерируем JWT токен
        String token = jwtUtil.generateToken(userDetails);
        
        return new LoginResponse(token, user.getUsername(), user.getEmail());
    }
}