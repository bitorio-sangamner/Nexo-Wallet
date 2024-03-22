package com.wallet.controller;

import com.wallet.payloads.TransactionDto;
import com.wallet.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/Transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/saveTransaction")
    public ResponseEntity<Object> createTransaction(@RequestBody TransactionDto transactionDto)
    {
        String message= transactionService.saveTransaction(transactionDto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
