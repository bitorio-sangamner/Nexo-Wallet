package com.authentication.controllers;

import com.authentication.dto.ApiResponse;
import com.authentication.dto.AuthRequest;
import com.authentication.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
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
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("email") String email, @RequestParam("token") String token) {
        return authService.verifyEmail(email, token);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> startVerificationProcess(@RequestParam("email") String email) {
        authService.startVerifyingProcess(email);
        var apiResponse = new ApiResponse(
                "Verification link has been sent to the registered email address.",
                "success",
                null);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/forgotpassword")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam("email") String email) {
        return authService.forgotPassword(email);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody AuthRequest authRequest) {
        System.out.println("inside reset password");
        return authService.resetPassword(authRequest);
    }

    @PostMapping("/logoff")
    @PreAuthorize("hasAnyAuthority({'ADMIN','USER'})")
    public ResponseEntity<ApiResponse> logout(@RequestParam("email") String email) {
        return authService.logout(email);
    }

    @PostMapping("/hello")
    public ResponseEntity<ApiResponse> hello() {
        return new ResponseEntity<>(new ApiResponse("hello", "pass", new Integer(8)), HttpStatus.OK);
    }
}
