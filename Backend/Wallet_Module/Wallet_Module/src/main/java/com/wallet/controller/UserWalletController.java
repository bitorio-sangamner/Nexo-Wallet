package com.wallet.controller;

import com.wallet.payloads.UserWalletDto;
import com.wallet.service.UserWalletService;
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

    @PostMapping("/createUserCoinsDetails/{userId}/{userName}")
    public void createUserCoinsDetails(@PathVariable Long userId,@PathVariable String userName)
    {
        userWalletService.createUserCoins(userId,userName);
    }

    @GetMapping("/getCurrencyHeldByUser/{userId}")
    public List<UserWalletDto> getCurrencyHeldByUser(@PathVariable Long userId)
    {
        List<UserWalletDto> allCoinsDetailsDto= userWalletService.getCurrencyHeldByUser(userId);
        return allCoinsDetailsDto;
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
