package com.authentication.controllers;

import com.authentication.entities.UserCoinsDto;
import com.authentication.repositories.UserRepository;
import com.authentication.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Nexousers/user")
public class UserCoinsController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/getAllCurrenciesHeldByUser")
    public ResponseEntity<Object> getAllCurrenciesHeldByUser(@CookieValue(name = "sessionIdentifier", required = false) String sessionIdentifier)
    {
        logger.info("Session identifier from cookie: {}", sessionIdentifier);
        System.out.println("email :"+sessionIdentifier);
        List<UserCoinsDto> allCurrenciesHeldByUer=userService.getAllCurrenciesHeldByUser(sessionIdentifier);

        return new ResponseEntity<>(allCurrenciesHeldByUer, HttpStatus.OK);

    }

}
