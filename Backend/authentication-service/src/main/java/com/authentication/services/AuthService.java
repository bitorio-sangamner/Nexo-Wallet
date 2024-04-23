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

    private final JwtUtil jwtUtil;

    /**
     * This method registers the user into system and starts verifying process for the user's account.
     * On any Messaging exception occurring in sending the email to user. The user's data is deleted and
     * InternalServerException is sent to the user.
     * @param authRequest record containing email and password and pin(if provided)
     * @return ResponseEntity with ApiResponse object with String message, time the response have been sent, true for success and null for object.
     * @throws InternalServerException for verify email link email not been able to be sent to the email address.
     * @author rsmalani
     */

    public ResponseEntity<ApiResponse> register(AuthRequest authRequest) {

        // Check for, is the user registered
        if (authUserRepository.findByEmail(authRequest.email().toLowerCase()).isPresent()) {
            var apiResponse = new ApiResponse("Email already registered.", "fail", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
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

        var apiResponse = new ApiResponse(
                "User Registered Successfully.",
                "success",
                null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * This method starts the verification process by setting verifying token, making the verification token state
     * active [/true] and the verification token generation time for user with given email and calls the sendVerifyEmail
     * method for sending email with verification link to the user.
     * @param email user's email address
     */
    public void startVerifyingProcess(String email) {
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
     */
    public ResponseEntity<ApiResponse> login(AuthRequest authRequest) {
        // Check for, is the user registered
        AuthUser user = authUserRepository
                .findByEmail(authRequest.email().toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.OK));

        // Checks for, is the user verified
        if (!user.isVerified()) {
            throw new EmailNotVerifiedException("Email is not verified.", HttpStatus.OK);
        }

        // Checks for, if the credentials are not correct
        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            log.error("User attempting to login with invalid password - {}", authRequest);

            var apiResponse = new ApiResponse("Enter correct user credentials.", "fail", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        // var is used in place of AuthResponse
        var response = new AuthResponse(
                jwtUtil.generate(authRequest.email(), "User", "ACCESS")
        );

        var apiResponse = new ApiResponse("User logged in successfully", "success", response);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * This method is used gor getting all users stored in authentication-service.
     * For ADMIN role only.
     * Priority update this method for large amount of users.
     * @return ResponseEntity with ApiResponse object containing all users
     */
    public ResponseEntity<ApiResponse> getAllUsers() {
        var response = authUserRepository.findAll();
        var apiResponse = new ApiResponse("Here are all the Users", "success", response);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
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
                .orElseThrow(() -> new EmailNotRegisteredException(email + ":- Email is not registered.", HttpStatus.OK));
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

        var apiResponse = new ApiResponse("Reset password link is sent to you registered email.", "success", null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
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
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.OK));

        // Checks if the user has started the reset password routine
        if (!user.isResetPasswordStateActive()) {
            throw new UnAuthorizedAccessException("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }

        long resetPasswordLinkExpirationTime = user.getResetPasswordLinkGenerationTime() + 300000;
        user.setResetPasswordStateActive(false);
        user.setResetPasswordLinkGenerationTime(0);

        // Checks if the user is using the link which has been expired
        if (Instant.now().toEpochMilli() > resetPasswordLinkExpirationTime) {
            authUserRepository.save(user);
            var apiResponse = new ApiResponse("Your reset password link is expired.","fail", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        user.setPassword(BCrypt.hashpw(authRequest.password(), BCrypt.gensalt()));
        authUserRepository.save(user);

        var apiResponse = new ApiResponse("Password reset successfully.", "success", null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
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
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.OK));

        // Checks if the user is already verified.
        if (user.isVerified()) {
            var apiResponse = new ApiResponse("Email is already verified.", "fail", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        // Checks if the user unknowingly access the link even if he has not accessed this endpoint
        if (!user.isVerifyEmailStateActive()) {
            throw new UnAuthorizedAccessException("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }

        long tokenExpirationTime = user.getVerifyEmailTokenGenerationTime() + 300000;
        String verifyToken = user.getVerifyToken();
        user.setVerifyToken(null);
        user.setVerifyEmailStateActive(false);
        user.setVerifyEmailTokenGenerationTime(0);

        // Checks if the verification link has expired
        if (Instant.now().getEpochSecond() > tokenExpirationTime) {
            authUserRepository.save(user);

            var apiResponse = new ApiResponse(
                    "Email verification link is expired. Please try again",
                    "fail",
                    null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        // Checks if the verification link has been tampered with.
        if (!verifyToken.equals(token)) {
            authUserRepository.save(user);

            var apiResponse = new ApiResponse(
                    "Invalid verification link used for account. Please try again",
                    "fail",
                    null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        user.setVerified(true);
        AuthUser authUser=authUserRepository.save(user);

        String url = """
                http://localhost:9091/wallet/createUserWallet/%d/%s""".formatted(1234567890, email);
        restTemplate.postForObject(url, null, Void.class, authUser.getId(), authUser.getEmail());

        var apiResponse = new ApiResponse("Email is verified.", "success", null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
