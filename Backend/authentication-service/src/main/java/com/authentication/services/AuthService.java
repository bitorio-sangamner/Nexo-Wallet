package com.authentication.services;

import com.authentication.dao.AuthUserRepository;
import com.authentication.dto.ApiResponse;
import com.authentication.dto.AuthRequest;
import com.authentication.dto.ResetPasswordRequest;
import com.authentication.entities.AuthUser;
import com.authentication.exceptions.EmailNotRegisteredException;
import com.authentication.exceptions.EmailNotVerifiedException;
import com.authentication.exceptions.InternalServerException;
import com.authentication.exceptions.UnAuthorizedAccessException;
import com.authentication.util.EmailUtil;
import com.authentication.security.util.JwtUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;


import java.lang.reflect.Field;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class handles all the business logic for the login, registration, forgot password, reset password and verify
 * email for the user into the system to be used for whole server wide application.
 *
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

    private RestClient restClient;

    private final JwtUtil jwtUtil;


    /**
     * This method registers the user into system and starts verifying process for the user's account.
     * On any Messaging exception occurring in sending the email to user. The user's data is deleted and
     * InternalServerException is sent to the user.
     *
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
        log.info("User registered successfully with email: {}", authRequest.email());

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
     *
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
        log.info("User has started verification process with email: {}", email);

        emailUtil.sendVerifyEmail(email, token);
    }

    /**
     * This method is used for log in the user into the system.
     *
     * @param authRequest record containing email and password and pin(if provided)
     * @return ResponseEntity with HTTPOnly cookie in headers containing JWT, time the response have been sent, true for success and AuthResponse with jwt token.
     * @throws EmailNotRegisteredException for attempting to log in with email not registered in system.
     * @throws EmailNotVerifiedException   for attempting to log in with email not verified by the system.
     */
    public ResponseEntity<ApiResponse> login(AuthRequest authRequest) {
        // Checks if the user is already registered
        AuthUser user = authUserRepository
                .findByEmail(authRequest.email().toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.OK.value()));

        // Checks for, is the user verified
        if (!user.isVerified()) {
            throw new EmailNotVerifiedException("Email is not verified.", HttpStatus.OK.value());
        }

        // Checks for, if the credentials are not correct
        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            log.error("User attempting to login with invalid password - {}", authRequest);

            var apiResponse = new ApiResponse("Enter correct user credentials.", "fail", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        // future code for starting new process if the user is already logged in.
        if (user.isLoggedIn()) {
            var apiResponse = new ApiResponse("User is already logged in.", "fail", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        user.setLoggedIn(true);
        authUserRepository.save(user);
        log.info("User logged in successfully with email: {}", authRequest.email());

        var apiResponse = new ApiResponse("User logged in successfully", "success", null);
        var cookie = jwtUtil.generateJwtCookie(user.getEmail(), user.getRoles(), null);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(apiResponse);
    }

    /**
     * This method is used for getting all users stored in authentication-service.
     * For ADMIN role only.
     * Priority update this method for large amount of users.
     *
     * @return ResponseEntity with ApiResponse object containing all users
     */
    public ResponseEntity<ApiResponse> getAllUsers() {
        var response = authUserRepository.findAll();
        var apiResponse = new ApiResponse("Here are all the Users", "success", response);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * This method is used to initiate the forgot password routine by the user.
     *
     * @param email user's email address
     * @return ResponseEntity with ApiResponse object with String message, time the response have been sent, true for success and null for object.
     * @throws EmailNotRegisteredException for attempting to log in with email not registered in system.
     * @throws InternalServerException     for reset password link email not been able to be sent to the email address.
     */
    public ResponseEntity<ApiResponse> forgotPassword(String email) {

        // Check for, is the user registered
        AuthUser user = authUserRepository
                .findByEmail(email.toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException(email + ":- Email is not registered.", HttpStatus.OK.value()));
        user.setResetPasswordStateActive(true);
        user.setResetPasswordLinkGenerationTime(Instant.now().toEpochMilli());
        AtomicReference<ApiResponse> apiResponse = new AtomicReference<>(new ApiResponse("Reset password link is sent to you registered email.", "success", null));
        log.info("User starting forgot password sequence with email: {}", email);
        // Asynchronously calls the sendResetPasswordEmail in emailUtil and to throw InternalServerException on any MessagingException error occurred during.
        CompletableFuture.runAsync(() -> emailUtil.sendResetPasswordEmail(email)).exceptionally(ex -> {
            user.setResetPasswordStateActive(false);
            user.setResetPasswordLinkGenerationTime(0);
            apiResponse.set(new ApiResponse("Server is busy right now. Please try again later.", "error", null));
            log.info("The email util is unavailable or message service is not working");
            return null;
        });

        authUserRepository.save(user);
        return new ResponseEntity<>(apiResponse.get(), HttpStatus.OK);
    }

    /**
     * This method is used to reset the user's password for the email.
     *
     * @param resetPasswordRequest record containing email and password and pin(if provided)
     * @return ResponseEntity with ApiResponse object with String message, time the response have been sent, true for success and null for object.
     * Unauthorized http code on if the user has not started the forgot password routine.
     * Ok code if the link has expired or correct code processing.
     * @throws EmailNotRegisteredException for attempting to use email not registered in system for resetting password.
     * @throws InternalServerException     for reset password link email not been able to be sent to the email address.
     */
    public ResponseEntity<ApiResponse> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        AuthUser user = authUserRepository
                .findByEmail(resetPasswordRequest.email().toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.OK.value()));

        // Checks if the user has started the reset password routine
        if (!user.isResetPasswordStateActive()) {
            log.error("User attempting to resetting password without forget password initiating - {}", resetPasswordRequest);
            throw new UnAuthorizedAccessException("Unauthorized access", HttpStatus.UNAUTHORIZED.value());
        }

        if (!passwordEncoder.matches(resetPasswordRequest.oldPassword(), user.getPassword())) {
            log.error("User attempting to reset password with invalid password - {}", resetPasswordRequest);
            var apiResponse = new ApiResponse("Enter valid previous password.", "fail", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        user.setResetPasswordStateActive(false);
        user.setResetPasswordLinkGenerationTime(0);
        long resetPasswordLinkExpirationTime = user.getResetPasswordLinkGenerationTime() + 300000;

        // Checks if the user is using the link which has been expired
        if (Instant.now().toEpochMilli() > resetPasswordLinkExpirationTime) {
            authUserRepository.save(user);
            var apiResponse = new ApiResponse("Your reset password link is expired.", "fail", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        user.setPassword(BCrypt.hashpw(resetPasswordRequest.newPassword(), BCrypt.gensalt()));
        authUserRepository.save(user);
        log.info("User has successfully reset password with email: {}", resetPasswordRequest.email());

        var apiResponse = new ApiResponse("Password reset successfully.", "success", null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    /**
     * This method is used to verify the user's email.
     *
     * @param email user's email address
     * @param token verification token
     * @return ResponseEntity with ApiResponse object with String message, time the response have been sent, true for success and null for object.
     * Unauthorized http code on if the user has unknowingly accessed this endpoint.
     * Bad-Request cde if the verification link is expired or tampered token.
     * Ok code if the user is already verified or on success.
     * @throws EmailNotRegisteredException for attempting to use email not registered in system for resetting password.
     */
    public ResponseEntity<ApiResponse> verifyEmail(String email, String token) {
        // Checks if the user is already registered
        AuthUser user = authUserRepository
                .findByEmail(email.toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.OK.value()));

        // Checks if the user is already verified.
        if (user.isVerified()) {
            var apiResponse = new ApiResponse("Email is already verified.", "fail", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        // Checks if the user unknowingly access the link even if he has not accessed this endpoint
        if (!user.isVerifyEmailStateActive()) {
            throw new UnAuthorizedAccessException("Unauthorized access", HttpStatus.UNAUTHORIZED.value());
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
        AuthUser authUser = authUserRepository.save(user);

        log.info("User has successfully verified his account with email: {}", email);



        String result = restClient.post()
                .uri("http://localhost:8080/api/subUser/create/{userId}/{email}/{password}", authUser.getId(), authUser.getEmail(),authUser.getPassword())
                .retrieve()
                .body(String.class);

        System.out.println(result);
        var apiResponse = new ApiResponse("Email is verified.", "success", null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    /**
     * This method is used for logging out the user from the system.
     * @param email User who wished to be logged out.
     * @return a API response with logged out message.
     */
    public ResponseEntity<ApiResponse> logout(String email) {

        // Checks if the user is already registered
        AuthUser user = authUserRepository
                .findByEmail(email.toLowerCase())
                .orElseThrow(() -> new EmailNotRegisteredException("Email is not registered.", HttpStatus.OK.value()));

        if (!user.isLoggedIn()) {
            var apiResponse = new ApiResponse("You have to be logged in.","fail", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        user.setLoggedIn(false);
        authUserRepository.save(user);

        log.info("User has logged out of the system successfully with email: {}", email);
        var apiResponse = new ApiResponse("You have been logged out successfully.","success", null);
        var cookie = jwtUtil.getCleanJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(apiResponse);
    }
}
