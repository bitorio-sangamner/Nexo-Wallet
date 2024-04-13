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
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * This class handles all the business logic for the login, registration, forgot password, reset password and verify
 * email for the user into the system to be used for whole server wide application.
 * @author rsmalani
 */
@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    /**
     * AuthUserRepository instance field for to used as DAO for database interactions.
     */
    private final AuthUserRepository authUserRepository;

    /**
     * PasswordEncoder instance field for password encoding and decoding.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * EmailUtil instance field for to be used for sending email.
     */
    private final EmailUtil emailUtil;

    private RestTemplate restTemplate;

    /**
     * This method registers the user into system and starts verifying process for the user's account.
     * On any Messaging exception occurring in sending the email to user. The user's data is deleted and
     * InternalServerException is sent to the user.
     * @param authRequest record containing email and password and pin(if provided)
     * @return ResponseEntity with ApiResponse object with String message, time the response have been sent, true for success and null for object.
     * @throws EmailAlreadyRegisteredException for registering with email already registered in system.
     * @throws InternalServerException for verify email link email not been able to be sent to the email address.
     * @author rsmalani
     */

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

        // Asynchronously calls the startVerifyingProcess and to throw InternalServerException on any MessagingException error occurred during.
        CompletableFuture.runAsync(() -> startVerifyingProcess(authRequest.email())).exceptionally(ex -> {
            if (ex instanceof MessagingException) {
                authUserRepository.delete(user);
            }
            throw new InternalServerException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        });

        return new ResponseEntity<>(new ApiResponse("User Registered Successfully. Email has been sent to email address to verify Email.", LocalDateTime.now(), true, null), HttpStatus.OK);
    }

    /**
     * This method starts the verification process by setting verifying token, making the verification token state
     * active [/true] and the verification token generation time for user with given email and calls the sendVerifyEmail
     * method for sending email with verification link to the user.
     * @param email user's email address
     */
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

    /**
     * This method is used for log in the user into the system.
     * @param authRequest record containing email and password and pin(if provided)
     * @return ResponseEntity with ApiResponse object with String message, time the response have been sent, true for success and AuthResponse with jwt token.
     * @throws EmailNotRegisteredException for attempting to log in with email not registered in system.
     * @throws EmailNotVerifiedException for attempting to log in with email not verified by the system.
     * @throws InvalidLoginAttemptException for attempting to log in with invalid email and password credentials.
     */
    public ResponseEntity<ApiResponse> login(AuthRequest authRequest) {
        // Check for, is the user registered
        AuthUser user = authUserRepository
                .findByEmail(authRequest.email().toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.CONFLICT));

        // Checks for, is the user verified
        if (!user.isVerified()) {
            throw new EmailNotVerifiedException("Email is not verified.", HttpStatus.CONFLICT);
        }

        // Checks for, if the credentials are not correct
        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            log.error("User attempting to login with invalid password - {}", authRequest);
            throw new InvalidLoginAttemptException("Enter correct user credentials.", HttpStatus.CONFLICT);
        }

        // var is used in place of AuthResponse
        var response = new AuthResponse(
                new JwtUtil().generate(authRequest.email(), "User", "ACCESS")
        );


        return new ResponseEntity<>(new ApiResponse("User logged in successfully", LocalDateTime.now(), true, response), HttpStatus.OK);
    }

    /**
     * This method is used gor getting all users stored in authentication-service.
     * For ADMIN role only.
     * Priority update this method for large amount of users.
     * @return ResponseEntity with ApiResponse object containing all users
     */
    public ResponseEntity<ApiResponse> getAllUsers() {
        var response = authUserRepository.findAll();
        return new ResponseEntity<>(new ApiResponse("Here are all the Users", LocalDateTime.now(), true, response), HttpStatus.OK);
    }

    /**
     * This method is used to initiate the forgot password routine by the user.
     * @param email user's email address
     * @return ResponseEntity with ApiResponse object with String message, time the response have been sent, true for success and null for object.
     * @throws EmailNotRegisteredException for attempting to log in with email not registered in system.
     * @throws InternalServerException for reset password link email not been able to be sent to the email address.
     */
    public ResponseEntity<ApiResponse> forgotPassword(String email) {

        // Check for, is the user registered
        AuthUser user = authUserRepository
                .findByEmail(email.toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.CONFLICT));
        user.setResetPasswordStateActive(true);
        user.setResetPasswordLinkGenerationTime(Instant.now().toEpochMilli());

        // Asynchronously calls the sendResetPasswordEmail in emailUtil and to throw InternalServerException on any MessagingException error occurred during.
        CompletableFuture.runAsync(() -> emailUtil.sendResetPasswordEmail(email)).exceptionally(ex -> {
            if (ex instanceof MessagingException) {
                user.setResetPasswordStateActive(false);
                user.setResetPasswordLinkGenerationTime(0);
            }
            throw new InternalServerException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        });
        return new ResponseEntity<>(new ApiResponse("Reset password link is sent to you registered email.", LocalDateTime.now(), true, null), HttpStatus.OK);
    }

    /**
     * This method is used to reset the user's password for the email.
     * @param authRequest record containing email and password and pin(if provided)
     * @return ResponseEntity with ApiResponse object with String message, time the response have been sent, true for success and null for object.
     * Unauthorized http code on if the user has not started the forgot password routine.
     * Ok code if the link has expired or correct code processing.
     * @throws EmailNotRegisteredException for attempting to use email not registered in system for resetting password.
     * @throws InternalServerException for reset password link email not been able to be sent to the email address.
     */
    public ResponseEntity<ApiResponse> resetPassword(AuthRequest authRequest) {
        AuthUser user = authUserRepository
                .findByEmail(authRequest.email().toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.CONFLICT));

        // Checks if the user has started the reset password routine
        if (!user.isResetPasswordStateActive()) {
            return new ResponseEntity<>(new ApiResponse("Unauthorized access", LocalDateTime.now(), false, null), HttpStatus.UNAUTHORIZED);
        }

        long resetPasswordLinkExpirationTime = user.getResetPasswordLinkGenerationTime() + 300000;
        user.setResetPasswordStateActive(false);
        user.setResetPasswordLinkGenerationTime(0);

        // Checks if the user is using the link which has been expired
        if (Instant.now().toEpochMilli() > resetPasswordLinkExpirationTime) {
            authUserRepository.save(user);
            return new ResponseEntity<>(new ApiResponse("Your reset password link is expired.", LocalDateTime.now(), false, null), HttpStatus.OK);
        }

        user.setPassword(BCrypt.hashpw(authRequest.password(), BCrypt.gensalt()));
        authUserRepository.save(user);
        return new ResponseEntity<>(new ApiResponse("Password reset successfully.", LocalDateTime.now(), true, null), HttpStatus.OK);
    }

    /**
     * This method is used to verify the user's email.
     * @param email user's email address
     * @param token verification token
     * @return ResponseEntity with ApiResponse object with String message, time the response have been sent, true for success and null for object.
     * Unauthorized http code on if the user has unknowingly accessed this endpoint.
     * Bad-Request cde if the verification link is expired or tampered token.
     * Ok code if the user is already verified or on success.
     * @throws EmailNotRegisteredException for attempting to use email not registered in system for resetting password.
     */
    public ResponseEntity<ApiResponse> verifyEmail(String email, String token) {
        // Check for, is the user registered
        AuthUser user = authUserRepository
                .findByEmail(email.toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.CONFLICT));

        // Checks if the user is already verified.
        if (user.isVerified()) {
            return new ResponseEntity<>(new ApiResponse("Email is already verified.", LocalDateTime.now(), true, null), HttpStatus.OK);
        }

        // Checks if the user unknowingly access the link even if he has not accessed this endpoint
        if (!user.isVerifyEmailStateActive()) {
            return new ResponseEntity<>(new ApiResponse("Unauthorized access", LocalDateTime.now(), false, null), HttpStatus.UNAUTHORIZED);
        }

        long tokenExpirationTime = user.getVerifyEmailTokenGenerationTime() + 300000;
        String verifyToken = user.getVerifyToken();
        user.setVerifyToken(null);
        user.setVerifyEmailStateActive(false);
        user.setVerifyEmailTokenGenerationTime(0);

        // Checks if the verification link has expired
        if (Instant.now().getEpochSecond() > tokenExpirationTime) {
            authUserRepository.save(user);
            startVerifyingProcess(email);
            return new ResponseEntity<>(new ApiResponse("Email verification link is expired. A new verification link is sent to the registered email", LocalDateTime.now(), false, null), HttpStatus.BAD_REQUEST);
        }

        // Checks if the verification link has been tampered with.
        if (!verifyToken.equals(token)) {
            authUserRepository.save(user);
            startVerifyingProcess(email);
            throw new InvalidVerifyTokenException("Invalid verification attempt for account. A new verification link is sent to the registered email", HttpStatus.BAD_REQUEST);
        }

        user.setVerified(true);
        AuthUser authUser=authUserRepository.save(user);

        String url = "http://localhost:9091/wallet/createUserWallet/{userId}/{userName}";
        restTemplate.postForObject(url, null, Void.class, authUser.getId(), authUser.getEmail());
        return new ResponseEntity<>(new ApiResponse("Email is verified.", LocalDateTime.now(), true, null), HttpStatus.OK);
    }
}
