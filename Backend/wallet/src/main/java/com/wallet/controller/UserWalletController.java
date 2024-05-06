package com.wallet.controller;

import com.wallet.exceptions.ResourceNotFoundException;
import com.wallet.payloads.ApiResponse;
import com.wallet.payloads.UserWalletDto;
import com.wallet.service.UserWalletBalanceService;
import com.wallet.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@Slf4j
public class UserWalletController {

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private UserWalletBalanceService userWalletBalanceService;

    @PostMapping("/create/{userId}/{email}")
    public String createUserWallet(@PathVariable Long userId, @PathVariable String email)
    {
        try {
            log.info("inside createUserWallet...");
            userWalletService.createWallet(userId, email);
            userWalletBalanceService.createUserWalletBalance(userId, email);
            return "wallet created";
        }
        catch(Exception e)
        {
            // Log the exception
            log.error(String.valueOf(e));
            e.printStackTrace();
            return "Failed to create wallet";
        }


    }

    @GetMapping("/getWallet/{userName}")
    public ResponseEntity<Object> getUserWallet(@PathVariable String userName) {
        try {
              List<UserWalletDto> userWalletDtoList=this.userWalletService.getWallet(userName);
            return ResponseEntity.ok(userWalletDtoList);
        }
        catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage(), false));
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An unexpected error occurred", false));
        }
    }
}
