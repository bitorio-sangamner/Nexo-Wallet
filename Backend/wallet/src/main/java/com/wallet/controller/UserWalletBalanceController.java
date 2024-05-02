package com.wallet.controller;

import com.wallet.service.UserWalletBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/walletBalance")
@Slf4j
public class UserWalletBalanceController {

    @Autowired
    private UserWalletBalanceService userWalletBalanceService;

    @PostMapping("/create/{userId}/{email}")
    public void createUserWalletBalance(@PathVariable Long userId, @PathVariable String email)
    {
        System.out.println("inside createUserWalletBalance");
        try {
            userWalletBalanceService.createUserWalletBalance(userId, email);
        }

        catch(Exception e)
        {
            // Log the exception
            log.error(String.valueOf(e));
            e.printStackTrace();
        }
    }

}
