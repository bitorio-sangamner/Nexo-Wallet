package com.authentication.controllers;

import com.authentication.payloads.UserDto;
import com.authentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Nexousers")
public class UserController {

    @Autowired
    private UserService userService;

    //POST - create user
    @PostMapping("/")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto)
    {
        UserDto createUserDto=this.userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }


}
