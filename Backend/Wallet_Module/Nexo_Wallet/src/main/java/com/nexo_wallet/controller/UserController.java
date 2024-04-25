package com.nexo_wallet.controller;

import com.nexo_wallet.payloads.UserDto;
import com.nexo_wallet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createUser/{userName}")
    public void createUser(@PathVariable String userName)
    {
        log.info("************Inside createUser controller*************");
        log.info("username :"+userName);

        UserDto userDto = UserDto.builder()
                .email(userName)  // Assuming userName is the email in this context
                .firstName("")
                .middleName("")
                .lastName("")
                .dateOfBirth(null)
                .gender("")
                .address("")
                .createdOn(null)
                .updatedOn(null)
                .build();

        userService.createUser(userDto);

    }
}
