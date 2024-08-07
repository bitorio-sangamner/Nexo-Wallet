package com.wallet.controller;


import com.wallet.payloads.ApiResponse;
import com.wallet.payloads.UserWalletDto;
import com.wallet.payloads.UserWalletResponseDto;
import com.wallet.service.UserWalletBalanceService;
import com.wallet.service.UserWalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallet")
@Slf4j
public class UserWalletController {

    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private UserWalletBalanceService userWalletBalanceService;

    @PostMapping("/create/{userId}/{email}/{subMemberId}")
    public ResponseEntity<Object> createWallet(@PathVariable Long userId, @PathVariable String email,@PathVariable String subMemberId)
    {
        log.info("inside createWallet controller...");
        String message=userWalletService.createWallet(userId,email,subMemberId);
        if(message.equals("Wallet created successfully!!")) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(message, "success",null));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("An unexpected error occurred", "error",null));
    }

    @GetMapping("/getWallet")
    public ResponseEntity<Object> getUserWallet() {
        // Get the email from SecurityContextHolder
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, List<UserWalletResponseDto>>userWalletResponseDtoMap=this.userWalletService.getWallet(email);

             var apiResponse = new ApiResponse(
               "User wallet data",
                "success",userWalletResponseDtoMap);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);

    }

    @GetMapping("/getWallet/{currencyAbb}")
    public ResponseEntity<Object>getWallet(@PathVariable String currencyAbb)
    {
        // Get the email from SecurityContextHolder
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,UserWalletResponseDto> userWalletResponseDtoMap=userWalletService.getWallet(email,currencyAbb);

        //return ResponseEntity.ok(userWalletResponseDtoMap);

        var apiResponse = new ApiResponse(
                "User wallet data",
                "success",userWalletResponseDtoMap);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

}
