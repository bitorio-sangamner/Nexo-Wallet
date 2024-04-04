package com.authentication.services;

import com.authentication.dao.AuthUserRepository;
import com.authentication.dto.ApiResponse;
import com.authentication.dto.AuthRequest;
import com.authentication.dto.AuthResponse;
import com.authentication.entities.AuthUser;
import com.authentication.exceptions.*;
import com.authentication.util.EmailUtil;
import com.authentication.util.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;


@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
//    private final RestClient restClient;
    private final JwtUtil jwtUtil;

    private final AuthUserRepository authUserRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailUtil emailUtil;

    public ResponseEntity<ApiResponse> register(AuthRequest authRequest) {

        // Check for, is the user registered
        if (authUserRepository.findByEmail(authRequest.email().toLowerCase()).isPresent()) {
            throw new EmailAlreadyRegisteredException("Email already registered.", HttpStatus.CONFLICT);
        }

        String password = BCrypt.hashpw(authRequest.password(), BCrypt.gensalt());
        AuthUser user = AuthUser.builder()
                .email(authRequest.email().toLowerCase())
                .password(password)
                .pin(authRequest.pin())
                .roles("USER")
                .isVerified(false)
                .build();
        authUserRepository.save(user);

        CompletableFuture.runAsync(() -> startVerifyingProcess(authRequest.email())).exceptionally(ex -> {
            if (ex instanceof MessagingException) {
                authUserRepository.delete(user);
            }
            throw new InternalServerException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        });

        return new ResponseEntity<>(new ApiResponse("User Registered Successfully. Email has been sent to email address to verify Email.", LocalDateTime.now(), true, null), HttpStatus.OK);
    }

    private void startVerifyingProcess(String email) {
        String token = RandomStringUtils.randomAlphanumeric(20);
        AuthUser authUser = authUserRepository.findByEmail(email).get();
        long now = Instant.now().toEpochMilli();

        authUser.setVerifyEmailTokenGenerationTime(now);
        authUser.setVerifyToken(token);
        authUser.setVerifyEmailStateActive(true);
        authUserRepository.save(authUser);

        emailUtil.sendVerifyEmail(email, token);
    }

    public ResponseEntity<ApiResponse> login(AuthRequest authRequest) {
        String password = BCrypt.hashpw(authRequest.password(), BCrypt.gensalt());

        // Check for, is the user registered
        AuthUser user = authUserRepository
                .findByEmail(authRequest.email().toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.CONFLICT));

        // Check for, is the user verified
        if (!user.isVerified()) {
            throw new EmailNotVerifiedException("Email is not verified.", HttpStatus.CONFLICT);
        }

        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            log.error("User attempting to login with invalid password - {}", authRequest);
            throw new InvalidLoginAttemptException("Enter correct user credentials.", HttpStatus.CONFLICT);
        }

        // var is used in place of AuthResponse
        var response = new AuthResponse(
                jwtUtil.generate(authRequest.email(), "User", "ACCESS")
        );

        return new ResponseEntity<>(new ApiResponse("User logged in successfully", LocalDateTime.now(), true, response), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> getAllUsers() {
        var response = authUserRepository.findAll();
        return new ResponseEntity<>(new ApiResponse("Here are all the Users", LocalDateTime.now(), true, response), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> forgotPassword(String email) {

        // Check for, is the user registered
        AuthUser user = authUserRepository
                .findByEmail(email.toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.CONFLICT));
        user.setResetPasswordStateActive(true);
        user.setResetPasswordLinkGenerationTime(Instant.now().toEpochMilli());

        CompletableFuture<Void> verifyEmail = CompletableFuture.runAsync(() -> {
            emailUtil.sendResetPasswordEmail(email);
        }).exceptionally(ex -> {
            throw new InternalServerException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        });
        return new ResponseEntity<>(new ApiResponse("Reset password link is sent to you registered email.", LocalDateTime.now(), true, null), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> resetPassword(AuthRequest authRequest) {
        AuthUser user = authUserRepository
                .findByEmail(authRequest.email().toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.CONFLICT));

        if (!user.isResetPasswordStateActive()) {
            return new ResponseEntity<>(new ApiResponse("Unauthorized access", LocalDateTime.now(), false, null), HttpStatus.UNAUTHORIZED);
        }

        long resetPasswordLinkExpirationTime = user.getResetPasswordLinkGenerationTime() + 300000;
        user.setResetPasswordStateActive(false);
        user.setResetPasswordLinkGenerationTime(0);

        if (Instant.now().toEpochMilli() > resetPasswordLinkExpirationTime) {
            authUserRepository.save(user);
            return new ResponseEntity<>(new ApiResponse("Your reset password link is expired.", LocalDateTime.now(), false, null), HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(BCrypt.hashpw(authRequest.password(), BCrypt.gensalt()));
        authUserRepository.save(user);
        return new ResponseEntity<>(new ApiResponse("Password reset successfully.", LocalDateTime.now(), true, null), HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse> verifyEmail(String email, String token) {
        // Check for, is the user registered
        AuthUser user = authUserRepository
                .findByEmail(email.toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.CONFLICT));

        if (user.isVerified()) {
            return new ResponseEntity<>(new ApiResponse("Email is already verified.", LocalDateTime.now(), true, null), HttpStatus.OK);
        }

        if (!user.isVerifyEmailStateActive()) {
            return new ResponseEntity<>(new ApiResponse("Unauthorized access", LocalDateTime.now(), false, null), HttpStatus.UNAUTHORIZED);
        }

        long tokenExpirationTime = user.getVerifyEmailTokenGenerationTime() + 300000;
        String verifyToken = user.getVerifyToken();

        user.setVerifyToken(null);
        user.setVerifyEmailStateActive(false);
        user.setVerifyEmailTokenGenerationTime(0);

        if (Instant.now().getEpochSecond() > tokenExpirationTime) {
            authUserRepository.save(user);
            startVerifyingProcess(email);
            return new ResponseEntity<>(new ApiResponse("Email verification link is expired. A new verification link is sent to the registered email", LocalDateTime.now(), false, null), HttpStatus.BAD_REQUEST);
        }

        if (!verifyToken.equals(token)) {
            authUserRepository.save(user);
            startVerifyingProcess(email);
            throw new InvalidVerifyTokenException("Invalid verification attempt for account. A new verification link is sent to the registered email", HttpStatus.BAD_REQUEST);
        }

        user.setVerified(true);
        authUserRepository.save(user);

        return new ResponseEntity<>(new ApiResponse("Email is verified.", LocalDateTime.now(), true, null), HttpStatus.OK);
    }
}
