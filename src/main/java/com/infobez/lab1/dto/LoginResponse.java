package com.infobez.lab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String email;
    
    public LoginResponse(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }
}