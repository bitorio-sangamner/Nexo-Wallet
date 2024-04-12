package com.authentication.controllers;

import com.authentication.config.jpaUserDetailsService.JpaUserDetailsService;
import com.authentication.dto.ApiResponse;
import com.authentication.dto.AuthRequest;
import com.authentication.services.AuthService;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    private JpaUserDetailsService userDetailsService;

    private static final Logger logger= LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody AuthRequest authRequest) {
        logger.info("inside register controller");
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

    @GetMapping("/forgotpassword")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody String email) {
        return authService.forgotPassword(email);
    }

    @GetMapping("/resetpassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody AuthRequest authRequest) {
        return authService.resetPassword(authRequest);
    }

    @GetMapping("/getUserDetails")
    public UserDetails getUserDetails(@PathVariable String userName)
    {
        UserDetails userDetails=userDetailsService.loadUserByUsername(userName);
        return userDetails;
    }
}
