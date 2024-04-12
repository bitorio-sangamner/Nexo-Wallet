package com.wallet.controller;

import com.wallet.payloads.UserWalletDto;
import com.wallet.service.UserWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet/userCoins")
public class UserWalletController {

    @Autowired
    private UserWalletService userWalletService;
    private static final Logger logger= LoggerFactory.getLogger(UserWalletController.class);

//    @PostMapping("/createUserWallet/{userId}/{userName}")
//    public void createUserWallet(@PathVariable Long userId,@PathVariable String userName)
//    {
//        userWalletService.createUserWallet(userId,userName);
//    }

    @GetMapping("/getCurrencyHeldByUser/{userId}")
    public List<UserWalletDto> getCurrencyHeldByUser(@PathVariable Long userId) {
        try {
            List<UserWalletDto> allCoinsDetailsDto = userWalletService.getCurrencyHeldByUser(userId);
            return allCoinsDetailsDto;
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            logger.error("Error getting currency held by user with ID: " + userId, e);
            // Return an appropriate response based on your application's logic
            // For simplicity, rethrowing the exception here
            throw e;
        }
    }


    @GetMapping("/getCoin/{userName}/{currencyName}")
    public ResponseEntity<Object> getCoin(@PathVariable String userName, @PathVariable String currencyName)
    {
         UserWalletDto userWalletDto=userWalletService.searchCoin(userName,currencyName);
         return new ResponseEntity<>(userWalletDto, HttpStatus.OK);
    }


    @GetMapping
    public List<UserWalletDto> getAllCoins() {
        return userWalletService.getAllCoinsOfUser();
    }
}
