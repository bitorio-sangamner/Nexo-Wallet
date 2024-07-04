package com.authentication.controllers;

import com.authentication.dto.ApiResponse;
import com.authentication.dto.AuthRequest;
import com.authentication.dto.ResetPasswordRequest;
import com.authentication.entities.AuthUser;
import com.authentication.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
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
    public ResponseEntity<ApiResponse> register(@RequestBody AuthRequest authRequest, HttpServletRequest servletRequest) {
        log.info("User registering from ip address: {} and email: {}", servletRequest.getRemoteAddr(), authRequest.email());
        return authService.register(authRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthRequest authRequest, HttpServletRequest servletRequest) {
        log.info("User signing in from ip address: {} and email: {}", servletRequest.getRemoteAddr(), authRequest.email());
        return authService.login(authRequest);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return authService.getAllUsers();
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("email") @Email String email, @RequestParam("token") String token, HttpServletRequest servletRequest) {
        log.info("User verifying from ip address: {} and email: {}", servletRequest.getRemoteAddr(), email);
        return authService.verifyEmail(email, token);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse> startVerificationProcess(@RequestParam("email") String email, HttpServletRequest servletRequest) {
        authService.startVerifyingProcess(email);
        var apiResponse = new ApiResponse(
                "Verification link has been sent to the registered email address.",
                "success",
                null);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/forgotpassword")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam("email") @Email String email, HttpServletRequest servletRequest) {
        log.info("User resetting password from ip address: {} and email: {}", servletRequest.getRemoteAddr(), email);
        return authService.forgotPassword(email);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest, HttpServletRequest servletRequest) {
        log.info("User resetting password from ip address: {} and email: {}", servletRequest.getRemoteAddr(), resetPasswordRequest.email());
        return authService.resetPassword(resetPasswordRequest);
    }

    @PostMapping("/logoff")
    @PreAuthorize("hasAnyAuthority({'ADMIN','USER'})")
    public ResponseEntity<ApiResponse> logout(@RequestParam("email") @Email String email, HttpServletRequest servletRequest) {
        return authService.logout(email);
    }

    @GetMapping("/getUser/{email}")
    public AuthUser getUser(@PathVariable String email)
    {
       return authService.getRegisteredUser(email);
    }
}
