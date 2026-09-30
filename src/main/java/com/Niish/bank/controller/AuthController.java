package com.Niish.bank.controller;

import com.Niish.bank.dto.ErrorResponse;
import com.Niish.bank.dto.LoginRequest;
import com.Niish.bank.dto.LoginResponse;
import com.Niish.bank.dto.RegisterRequest;
import com.Niish.bank.dto.RegisterResponse;
import com.Niish.bank.model.User;
import com.Niish.bank.service.AuthService;
import com.Niish.bank.service.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(
            AuthService authService,
            TokenBlacklistService tokenBlacklistService) {

        this.authService = authService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        User user = authService.register(request);

        RegisterResponse response = new RegisterResponse(
                user.getUsername(),
                user.getName(),
                user.getAccountNumber(),
                user.getBalance()
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(response);
    }

    // =========================
    // LOGOUT
    // =========================
    @PostMapping("/logout")
    public ResponseEntity<ErrorResponse> logout(
            HttpServletRequest request) {

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            ErrorResponse response = new ErrorResponse(
                    false,
                    "Authorization token is missing"
            );

            return ResponseEntity
                    .badRequest()
                    .body(response);
        }

        String token = authHeader.substring(7);

        tokenBlacklistService.blacklistToken(token);

        ErrorResponse response = new ErrorResponse(
                true,
                "Logout successful"
        );

        return ResponseEntity.ok(response);
    }
}