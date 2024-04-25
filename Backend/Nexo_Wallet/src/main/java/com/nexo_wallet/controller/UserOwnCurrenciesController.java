package com.nexo_wallet.controller;

import com.nexo_wallet.entity.UserOwnCurrencies;
import com.nexo_wallet.service.UserOwnCurrenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/userOwnCurrencies")
public class UserOwnCurrenciesController {

    @Autowired
    private UserOwnCurrenciesService userOwnCurrenciesService;

    @GetMapping("/byEmail")
    public ResponseEntity<List<UserOwnCurrencies>> getUserOwnCurrenciesByEmail(@RequestParam("email") String email) {
        try {
            List<UserOwnCurrencies> userOwnCurrenciesList = userOwnCurrenciesService.getUserOwnCurrenciesByEmail(email);
            if (userOwnCurrenciesList.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(userOwnCurrenciesList);
            }
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getUserCurrency")
    public ResponseEntity<UserOwnCurrencies> getUserCurrencyByUserEmailAndCurrencyName(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("currencyName") String currencyName) {
        UserOwnCurrencies userCurrency = userOwnCurrenciesService.getUserCurrencyByUserEmailAndCurrencyName(userEmail, currencyName);
        if (userCurrency != null) {
            return ResponseEntity.ok(userCurrency);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
