package com.nexo_wallet.controller;


import com.nexo_wallet.exceptions.ResourceNotFoundException;
import com.nexo_wallet.payloads.ApiResponse;
import com.nexo_wallet.payloads.WalletDto;
import com.nexo_wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/getWallet/{userName}")
    public ResponseEntity<Object> getUserWallet(@PathVariable String userName) {
        try {
            WalletDto walletDto = walletService.getWalletByEmailWithCurrencies(userName);
            return ResponseEntity.ok(walletDto);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage(), false));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An unexpected error occurred", false));
        }
    }
}
