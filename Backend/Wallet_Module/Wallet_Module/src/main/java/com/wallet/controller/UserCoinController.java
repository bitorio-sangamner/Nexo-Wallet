package com.wallet.controller;

import com.wallet.payloads.UserCoinsDto;
import com.wallet.service.UserCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userCoins")
public class UserCoinController {

    @Autowired
    private UserCoinService userCoinService;

    @PostMapping("/createUserCoinsDetails/{userId}")
    public void createUserCoinsDetails(@PathVariable Long userId)
    {
        userCoinService.createUserCoins(userId);
    }

    @GetMapping("/getCurrencyHeldByUser/{userId}")
    public List<UserCoinsDto> getCurrencyHeldByUser(@PathVariable Long userId)
    {
        List<UserCoinsDto> allCoinsDetailsDto=userCoinService.getCurrencyHeldByUser(userId);
        return allCoinsDetailsDto;
    }

    @GetMapping
    public List<UserCoinsDto> getAllCoins() {
        return userCoinService.getAllCoinsOfUser();
    }
}
