package com.example.apartment_manager.service.Impl;

import com.example.apartment_manager.dto.request.AuthRequest;
import com.example.apartment_manager.dto.request.RegisterRequest;
import com.example.apartment_manager.dto.response.UserResponse;
import com.example.apartment_manager.entity.Role;
import com.example.apartment_manager.entity.User;
import com.example.apartment_manager.repository.RoleRepository;
import com.example.apartment_manager.security.custom.CustomUserDetails;
import com.example.apartment_manager.security.custom.CustomUserDetailsService;
import com.example.apartment_manager.security.jwt.JwtTokenProvider;
import com.example.apartment_manager.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public String login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    @Override
    public String register(RegisterRequest registerRequest) {
        if (userService.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }
        if (!registerRequest.getPassword().equals(registerRequest.getRetypePassword())) {
            throw new IllegalArgumentException("Passwords do not match!");
        }
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(userRole);
        userService.saveUser(newUser);
        return "User registered successfully!";
    }

    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new SecurityException("User not authenticated!");
        }

        return getUserResponse((CustomUserDetails) authentication.getPrincipal());
    }

    private UserResponse getUserResponse(CustomUserDetails principal) {
        System.out.println(principal);
        return userService.findByUsername(principal.getUsername())
                .map(UserResponse::fromEntity)
                .orElseGet(UserResponse::new);
    }
}
