package com.wallet.controller;

import com.wallet.payloads.ApiResponse;
import com.wallet.payloads.UserWalletBalanceDto;
import com.wallet.service.UserWalletBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/walletBalance")
@Slf4j
public class UserWalletBalanceController {

    @Autowired
    private UserWalletBalanceService userWalletBalanceService;

    @GetMapping("/getUserCurrency")
    public ResponseEntity<UserWalletBalanceDto> getUserCurrencyByUserEmailAndCurrencyName(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("currencyName") String currencyName) {
            UserWalletBalanceDto userCurrency = userWalletBalanceService.getUserCurrencyByUserEmailAndCurrencyName(userEmail, currencyName);
            return ResponseEntity.ok(userCurrency);

    }

    @GetMapping("/getUserWalletBalance/{userName}")
    public ResponseEntity<Object> getUserWalletBalance(@PathVariable String userName)
    {
        List<UserWalletBalanceDto> userWalletBalanceDtoList=this.userWalletBalanceService.getWalletBalance(userName);
        var apiResponse = new ApiResponse(
                "User wallet data",
                true,userWalletBalanceDtoList);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);

    }


}
