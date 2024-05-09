package com.wallet.controller;

import com.wallet.payloads.CurrencyDTO;
import com.wallet.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @PostMapping("/save")
    public ResponseEntity<CurrencyDTO> saveCurrency(@RequestBody CurrencyDTO currencyDTO) {

        CurrencyDTO savedCurrency = currencyService.saveCurrency(currencyDTO);
        return new ResponseEntity<>(savedCurrency, HttpStatus.CREATED);
    }
}