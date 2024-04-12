package com.wallet.controller;

import com.wallet.service.UserWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private UserWalletService userWalletService;
    private static final Logger logger= LoggerFactory.getLogger(UserWalletController.class);

    @PostMapping("/createUserWallet/{userId}/{userName}")
    public void createUserWallet(@PathVariable Long userId, @PathVariable String userName)
    {
        userWalletService.createUserWallet(userId,userName);
    }
}
