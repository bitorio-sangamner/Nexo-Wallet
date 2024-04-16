package com.wallet.controller;

import com.wallet.payloads.UserDto;
import com.wallet.service.UserService;
//import com.wallet.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
@Slf4j

@RestController
@RequestMapping("/wallet")
public class WalletController {

//    @Autowired
//    private UserWalletService userWalletService;

    @Autowired
    private UserService userService;
   // private static final Logger logger= LoggerFactory.getLogger(UserWalletController.class);

    @PostMapping("/createUserWallet/{userName}")
    public void createUserWallet(@PathVariable String userName)
    {
        log.info("************Inside createUserWallet controller*************");
        log.info("username :"+userName);
//        userWalletService.createUserWallet(userId,userName);
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

        userService.saveUser(userDto);

    }
}
