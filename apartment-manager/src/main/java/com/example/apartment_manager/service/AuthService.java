package com.example.apartment_manager.service;

import com.example.apartment_manager.dto.request.AuthRequest;
import com.example.apartment_manager.dto.request.RegisterRequest;
import com.example.apartment_manager.dto.response.UserResponse;

public interface AuthService {
    String login(AuthRequest authRequest);
    String register(RegisterRequest authRequest);
    UserResponse getCurrentUser();
}
