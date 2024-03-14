package com.authentication.controllers;

import com.alibaba.fastjson.JSONObject;
import com.authentication.payloads.UserDto;
import com.authentication.services.UserService;
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
    public ResponseEntity<Object> signUp(HttpServletResponse response, @Valid @RequestBody UserDto userDto) throws Exception {
        String message;

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


        return new ResponseEntity<>(message,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/verify-account")
    public ResponseEntity<Object> verifyAccount(@RequestParam String email,@RequestParam String otp) throws Exception{


            String message = userService.verifyAccount(email, otp);
            if("Account verified. You can now login.".equals(message))
            {
                return new ResponseEntity<>(message,HttpStatus.OK);
            }
            else {
                 return new ResponseEntity<>(message,HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<Object> login(@RequestBody JSONObject jsonObject) throws Exception{

            String email = jsonObject.getString("email");
            String password = jsonObject.getString("password");

            String message = userService.login(email, password);
            logger.info("Message :"+message);

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
    }
}
