package com.wallet.controller;

import com.alibaba.fastjson.JSONObject;
import com.wallet.payloads.TransactionDto;
import com.wallet.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/Transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @PostMapping("/saveTransaction")
    public ResponseEntity<Object> createTransaction(@RequestBody TransactionDto transactionDto)
    {
        String message= transactionService.saveTransaction(transactionDto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @GetMapping("/filterTransaction")
    public ResponseEntity<List<TransactionDto>> filterTransaction(@RequestBody JSONObject jsonObject)
    {
        Long userId=jsonObject.getLongValue("userId");
        String cryptocurrency= jsonObject.getString("cryptocurrency");
        Date startDate=jsonObject.getDate("startDate");
        Date endDate=jsonObject.getDate("endDate");
        Date transactionDate=jsonObject.getDate("transactionDate");
        String transactionType= jsonObject.getString("transactionType");
        BigDecimal fiatValue=jsonObject.getBigDecimal("fiatValue");

        logger.info("inside filterTransaction controller");
        logger.info("transactionType :"+transactionType);
        logger.info("cryptocurrency :"+cryptocurrency);
        logger.info("fiatValue :"+fiatValue);

        List<TransactionDto> filteredTransactionList=transactionService.filterTransactions(userId, cryptocurrency, startDate, endDate,transactionDate, transactionType, fiatValue);

        return new ResponseEntity<>(filteredTransactionList,HttpStatus.OK);
    }

    @GetMapping("/searchTransaction")
    public ResponseEntity<Object> searchTransactionById(@RequestBody JSONObject jsonObject)
    {
       Long transactionId=jsonObject.getLongValue("transactionId");
       TransactionDto transactionDto=transactionService.searchTransactionById(transactionId);
       return new ResponseEntity<>(transactionDto,HttpStatus.FOUND);
    }
}
