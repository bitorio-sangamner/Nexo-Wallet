package com.authentication.controllers;

import com.alibaba.fastjson.JSONObject;
import com.authentication.payloads.UserDto;
import com.authentication.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/Nexousers")
public class UserController {

    @Autowired
    private UserService userService;

    //POST - create user
    @PostMapping("/SignUp")
    public ResponseEntity<UserDto> signUp(HttpServletResponse response, @Valid @RequestBody UserDto userDto) throws Exception {
        UserDto createUserDto=this.userService.registerUser(userDto);

        // Generate session identifier
        String sessionIdentifier=userDto.getEmail();

        // Create a cookie with the session identifier
        Cookie cookie = new Cookie("sessionIdentifier", sessionIdentifier);
        cookie.setHttpOnly(true); // Ensure the cookie is only accessible via HTTP and not by JavaScript
        cookie.setMaxAge(3600); // Cookie expires after 1 hour (adjust as needed)
        cookie.setPath("/"); // Cookie is accessible across the entire application

        // Add the cookie to the response headers
        response.addCookie(cookie);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    @PostMapping("/setPin")
    public ResponseEntity<Object> setPin(@CookieValue(name = "sessionIdentifier", required = false) String sessionIdentifier, @RequestBody JSONObject jsonObject)
    {

        String pin = jsonObject.getString("pin");
        if (sessionIdentifier != null) {
            String msg=userService.setPin(sessionIdentifier,pin);
            if(msg.equals("Pin set successfully!")) {
                //return msg;

                return new ResponseEntity<>(msg,HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(msg,HttpStatus.OK);
            }
        }
        else {
            return new ResponseEntity<>("something went wrong provide your email and password again",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody JSONObject jsonObject)
    {
        String email=jsonObject.getString("email");
        String password=jsonObject.getString("password");

        String msg=userService.login(email,password);

        if(msg.equals("user found"))
        {
            return new ResponseEntity<>(msg,HttpStatus.OK);
        }
        else if(msg.equals("invalid password"))
        {
            return new ResponseEntity<>(msg,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            return new ResponseEntity<>(msg,HttpStatus.NOT_FOUND);
        }

    }
}
