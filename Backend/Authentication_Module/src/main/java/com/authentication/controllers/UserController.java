package com.authentication.controllers;

import com.alibaba.fastjson.JSONObject;
import com.authentication.payloads.SetPasswordDto;
import com.authentication.payloads.UserDto;
import com.authentication.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Nexousers")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(HttpServletResponse response, @Valid @RequestBody UserDto userDto) {
        String message;
        try {
            // Register user and get user details
            // UserDto createdUserDto = userService.registerUser(userDto);
            message= userService.registerUser(userDto);
            if (message.equals("User registration successful check your email and verify your account")) {

                // Generate session identifier
                String sessionIdentifier = userDto.getEmail(); // For simplicity, using email as session identifier

                // Create a cookie with the session identifier
                Cookie cookie = new Cookie("sessionIdentifier", sessionIdentifier);
                cookie.setHttpOnly(true); // Ensure the cookie is only accessible via HTTP and not by JavaScript
                cookie.setMaxAge(3600); // Cookie expires after 1 hour (adjust as needed)
                cookie.setPath("/"); // Cookie is accessible across the entire application

                // Add the cookie to the response headers
                response.addCookie(cookie);
                logger.info("User signed up successfully: {}", userDto.getEmail());

                return new ResponseEntity<>(message, HttpStatus.CREATED);
            }
        }
        catch (Exception e) {
            logger.error("Failed to sign up user", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return new ResponseEntity<>(message,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email,@RequestParam String otp) {
        try {
//            String email = jsonObject.getString("email");
//            String otp = jsonObject.getString("otp");

            String message = userService.verifyAccount(email, otp);
            return ResponseEntity.ok(message);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

//    @PutMapping("/regenerate-otp")
//    public ResponseEntity<String> regenerateOtp(@RequestParam String email)
//    {
//
//    }




//    @PostMapping("/setPin")
//    public ResponseEntity<Object> setPin(@CookieValue(name = "sessionIdentifier", required = false) String sessionIdentifier, @RequestBody JSONObject jsonObject)
//    {
//
//        String pin = jsonObject.getString("pin");
//        if (sessionIdentifier != null) {
//            String msg=userService.setPin(sessionIdentifier,pin);
//            if(msg.equals("Pin set successfully!")) {
//                //return msg;
//
//                return new ResponseEntity<>(msg,HttpStatus.OK);
//            }
//            else {
//                return new ResponseEntity<>(msg,HttpStatus.OK);
//            }
//        }
//        else {
//            return new ResponseEntity<>("something went wrong provide your email and password again",HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody JSONObject jsonObject) {
        try {
            String email = jsonObject.getString("email");
            String password = jsonObject.getString("password");

            String message = userService.login(email, password);

            if ("user found".equals(message)) {
                logger.info("User logged in successfully: {}", email);
                return ResponseEntity.ok(message);
            }
            else if ("invalid password".equals(message)) {
                logger.warn("Invalid password for user: {}", email);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
            }
            else {
                logger.warn("User not found: {}", email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            }
        } catch (Exception e) {

            logger.error("An unexpected error occurred during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred. Please try again later.");
        }
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody JSONObject json) {
        try {
            String email = json.getString("email");
            String message = userService.forgotPassword(email);

            if ("please check your email to set a new password for your account".equals(message)) {
                logger.info("Forgot password request processed successfully for email: {}", email);
                return ResponseEntity.ok(message);
            }
            else {
                logger.warn("Failed to process forgot password request for email: {}. Reason: {}", email, message);
                return ResponseEntity.badRequest().body(message);
            }
        }
        catch (MessagingException e) {
            // Log the exception for debugging purposes
            logger.error("Failed to send email for forgot password request", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email. Please try again later.");
        }
        catch (Exception e) {
            // Log the exception for debugging purposes
            logger.error("An unexpected error occurred during forgot password request", e);
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again later.");
        }
    }
    @PutMapping("/set-password")
    public ResponseEntity<String> setPassword(@Valid @RequestBody SetPasswordDto setPasswordDto) {
        try {
            String email = setPasswordDto.getEmail();
            String newPassword = setPasswordDto.getNewPassword();

            String message = userService.setPassword(email, newPassword);

            HttpStatus status;
            if (message.equals("New password set successfully login with new password")) {
                status = HttpStatus.OK;
            }
            else if (message.equals("User not found with email: " + email)) {
                status = HttpStatus.NOT_FOUND;
            }
            else {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            return ResponseEntity.status(status).body(message);
        }
        catch (Exception e) {

            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to set new password. Please try again later.");
        }
    }
}
