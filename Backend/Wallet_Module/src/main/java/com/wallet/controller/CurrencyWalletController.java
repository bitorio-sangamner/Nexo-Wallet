package com.wallet.controller;

import com.wallet.payloads.CurrencyWalletDto;
import com.wallet.service.CurrencyWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallet/userCoins")
public class CurrencyWalletController {

    @Autowired
    private CurrencyWalletService currencyWalletService;
    private static final Logger logger= LoggerFactory.getLogger(CurrencyWalletController.class);
    @GetMapping("/getCurrencyHeldByUser/{userName}")
    public List<CurrencyWalletDto> getCurrencyHeldByUser(@PathVariable String userName) {
        try {
            List<CurrencyWalletDto> allCoinsDetailsDto = currencyWalletService.getCurrencyHeldByUser(userName);
            return allCoinsDetailsDto;
        } catch (Exception e) {
            // Log the exception or handle it appropriately
            logger.error("Error getting currency held by user with ID: " + userName, e);
            // Return an appropriate response based on your application's logic
            // For simplicity, rethrowing the exception here
            throw e;
        }
    }


    @GetMapping("/getCoin/{userName}/{currencyName}")
    public ResponseEntity<Object> getCoin(@PathVariable String userName, @PathVariable String currencyName)
    {
         CurrencyWalletDto currencyWalletDto = currencyWalletService.searchCoin(userName,currencyName);
         return new ResponseEntity<>(currencyWalletDto, HttpStatus.OK);
    }


//    @GetMapping
//    public List<UserWalletDto> getAllCoins() {
//        return userWalletService.getAllCoinsOfUser();
//    }
}
