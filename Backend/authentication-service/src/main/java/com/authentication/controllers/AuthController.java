package com.authentication.controllers;

import com.authentication.dto.ApiResponse;
import com.authentication.dto.AuthRequest;
import com.authentication.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody AuthRequest authRequest) {
        return authService.register(authRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return authService.getAllUsers();
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("email") String email) {
        return authService.verifyEmail(email);
    }

    @GetMapping("/forgotpassword")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody String email) {
        return authService.forgotPassword(email);
    }

    @GetMapping("/resetpassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody AuthRequest authRequest) {
        return authService.resetPassword(authRequest);
    }
}
